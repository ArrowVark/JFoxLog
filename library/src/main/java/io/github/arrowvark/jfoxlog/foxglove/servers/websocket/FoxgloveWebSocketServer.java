package io.github.arrowvark.jfoxlog.foxglove.servers.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;
import io.github.arrowvark.jfoxlog.defaults.topics.FieldLogger;
import io.github.arrowvark.jfoxlog.defaults.topics.OriginLogger;
import io.github.arrowvark.jfoxlog.defaults.topics.RobotLogger;
import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.servers.Util;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class FoxgloveWebSocketServer extends WebSocketServer {

    private static final FoxgloveWebSocketServer INSTANCE;
    private static final int port = 5820;

    static { // Create instance
        INSTANCE = new FoxgloveWebSocketServer(port);
        INSTANCE.start();
    }

    private static String NAME = "FRC Robot Server";
    private static final int SESSION_INT = ThreadLocalRandom.current().nextInt(100000, 1000000);
    private static String SESSION_ID = "frc-session-" + SESSION_INT;

    public static final Gson WEBSOCKET_GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final List<FoxgloveWebSocketConnection> connections = new ArrayList<>();
//    private static final List<FoxgloveLoggable> loggableObjects = new ArrayList<>();
    private static final List<FoxgloveChannel<? extends FoxgloveLoggable>> channels = new ArrayList<>();

    private static final AtomicInteger highestUsedId = new AtomicInteger(-1);
//    private final java.util.Map<WebSocket, java.util.Map<Integer, Integer>> connectionSubscriptions =
//            new java.util.concurrent.ConcurrentHashMap<>();

//    private static final FoxgloveChannel MAIN_FRAME_CHANNEL = requestNewChannel(
//            "Server/Frames/Main",
//            new FoxgloveTypes.FrameTransform(
//                    FoxgloveTypes.Timestamp.now(),
//                    "",
//                    "main",
//                    new FoxgloveTypes.Vector3(0, 0, 0),
//                    new FoxgloveTypes.Quaternion(0, 0, 0, 0)
//            )
//    ).setOnBroadcast(() -> new FoxgloveTypes.FrameTransform(
//            FoxgloveTypes.Timestamp.now(),
//            "",
//            "main",
//            new FoxgloveTypes.Vector3(0, 0, 0),
//            new FoxgloveTypes.Quaternion(0, 0, 0, 0)
//    ));

    public static final JsonObject SERVER_INFO;
    public static final String SERVER_INFO_STRING;

    static { // Create server info
        JsonObject o = new JsonObject();
        o.addProperty("op", "serverInfo");
        o.addProperty("name", NAME);
        o.addProperty("sessionId", SESSION_ID);
        o.add("capabilities", new JsonArray());
        JsonArray encodings = new JsonArray();
        encodings.add("json");
        o.add("supportedEncodings", encodings);
        JsonObject meta = new JsonObject();
        o.add("metadata", meta);

        SERVER_INFO = o;
        SERVER_INFO_STRING = WEBSOCKET_GSON.toJson(o);
    }

    static { // Init static topics
        FieldLogger.init();
        OriginLogger.init();
        RobotLogger.init(); // Temp
    }

    private FoxgloveWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    public static void setName(String name) {
        NAME = name;
    }

    public static void setSessionId(String sessionId) {
        SESSION_ID = sessionId;
    }

    public static FoxgloveWebSocketServer getInstance() {
        return INSTANCE;
    }

    public void periodic() {
        // TODO: Dumb messaging for now, may change to a smarter system later
        for (FoxgloveWebSocketConnection conn : connections) {
            conn.advertiseAllUnadvertisedChannels();
            conn.broadcastAllServerDrivenChannels();
        }
    }

    public static void registerChannel(FoxgloveChannel<? extends FoxgloveLoggable> channel) {
        for (FoxgloveChannel<? extends FoxgloveLoggable> sub : channels) {
            if (Objects.equals(sub.getTopic(), channel.getTopic())) {
                Logger.getGlobal().warning("Duplicate channel topic: " + channel.getTopic());
            }
        }

        channels.add(channel);

        for (FoxgloveWebSocketConnection connection : connections) {
            connection.addChannel(channel);
        }
    }

    public static void attemptToBroadcastChannel(FoxgloveChannel<? extends FoxgloveLoggable> channel) {
        if (INSTANCE == null) return;

        for (FoxgloveWebSocketConnection connection : connections) {
            if (connection.unadvertisedChannels().contains(channel)) {
                connection.advertiseAllUnadvertisedChannels();
            }
        }

        INSTANCE.broadcastChannelToAllConnections(channel);
    }

    public static void attemptToRunAdvertisementCheck() {
        if (INSTANCE == null) return;

        INSTANCE.runFullAdvertisementCheck();
    }

    public static <T extends FoxgloveLoggable> FoxgloveChannel<T> requestNewChannel(String topic, FoxgloveChannel.LoggingType loggingType, T loggable) {
        var channel = new FoxgloveChannel<>(highestUsedId.incrementAndGet(), topic, loggingType, loggable);
        registerChannel(channel);

        return channel;
    }

    /**
     * Returns true if there is a channel matching the given id
     *
     * @param channelId
     * @return
     */
    public static boolean channelInUse(int channelId) {
        for (FoxgloveChannel<? extends FoxgloveLoggable> channel : channels) {
            if (channel.getId() == channelId) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(
            WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {

        ServerHandshakeBuilder builder =
                super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);

        String requestedProtocol = request.getFieldValue("Sec-WebSocket-Protocol");
        if (requestedProtocol != null && requestedProtocol.contains("foxglove.websocket.v1")) {
            builder.put("Sec-WebSocket-Protocol", "foxglove.websocket.v1");
        } else {
            // Reject connections that aren't from Foxglove
            throw new InvalidDataException(
                    CloseFrame.POLICY_VALIDATION, "Unsupported subprotocol");
        }

        return builder;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Client connected: " + conn.getRemoteSocketAddress());

        for (FoxgloveWebSocketConnection connection : connections) {
            if (connection.webSocketMatchesConnection(conn)) {
                System.out.println("Connection: " + conn.getRemoteSocketAddress() + " matches an already existing" +
                        "connection. This may be from a reconnection, no action is needed");
                return;
            }
        }

        FoxgloveWebSocketConnection newConnection = new FoxgloveWebSocketConnection(conn, channels);
        connections.add(newConnection);
        newConnection.broadcastServerInfo();
        newConnection.advertiseAllUnadvertisedChannels();
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

        for (FoxgloveWebSocketConnection connection : connections) {
            if (connection.webSocketMatchesConnection(conn)) {
                connections.remove(connection);
                break;
            }
        }

        String remoteText = remote ? "(remote)" : "(local)";
        System.out.println("Client disconnected: " + code + " " + reason + " " + remoteText);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        JsonObject json = WEBSOCKET_GSON.fromJson(message, JsonObject.class);
        String op = json.get("op").getAsString();
        FoxgloveWebSocketConnection targetConnection = null;

        for (FoxgloveWebSocketConnection connection : connections) {
            if (connection.webSocketMatchesConnection(conn)) {
                targetConnection = connection;
                break;
            }
        }

        if (targetConnection == null) {
            Logger.getGlobal().warning("Received a message on a connection not known to the server" +
                    "\nMessage from unknown client: " + conn.getRemoteSocketAddress());
            return;
        }

        if (op.equals("subscribe")) {
//            java.util.Map<Integer, Integer> subMap =
//                    connectionSubscriptions.computeIfAbsent(conn, k -> new java.util.HashMap<>());

            JsonArray subscriptions = json.getAsJsonArray("subscriptions");
            for (var element : subscriptions) {
                JsonObject sub = element.getAsJsonObject();
                int subId     = sub.get("id").getAsInt();
                int channelId = sub.get("channelId").getAsInt();
                targetConnection.subscribe(channelId, subId);
            }

        } else if (op.equals("unsubscribe")) {
//            java.util.Map<Integer, Integer> subMap = connectionSubscriptions.get(conn);
//            if (subMap == null) return;

            JsonArray subscriptionIds = json.getAsJsonArray("subscriptionIds");
            for (var element : subscriptionIds) {
                int removedSubId = element.getAsInt();
                targetConnection.unsubscribe(removedSubId);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception e) {

    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started, view robot data in Foxglove at: https://app.foxglove.dev/~/view?ds=foxglove-websocket&ds.url=ws://" + Util.getLocalIp() + ":5820");
    }

//    public void broadcastJsonMessageOnChannel(String msg, int channelId) {
//        Instant now = Instant.now();
//        long epochNano = now.toEpochMilli() * 1_000_000L + (now.getNano() % 1_000_000L);
//        byte[] payloadBytes = msg.getBytes(StandardCharsets.UTF_8);
//
//        for (java.util.Map.Entry<WebSocket, java.util.Map<Integer, Integer>> entry
//                : connectionSubscriptions.entrySet()) {
//
//            WebSocket conn = entry.getKey();
//            Integer subscriptionId = entry.getValue().get(channelId);
//            if (subscriptionId == null) continue;
//
//            // op (1 byte) + id (4 bytes) + timestamp (8 bytes) + data
//            ByteBuffer buffer = ByteBuffer.allocate(13 + payloadBytes.length)
//                    .order(ByteOrder.LITTLE_ENDIAN);
//
//            buffer.put((byte) 0x01); // op
//            buffer.putInt(subscriptionId); // id
//            buffer.putLong(epochNano); // timestamp
//            buffer.put(payloadBytes); // data
//            buffer.flip();
//
//            conn.send(buffer);
//        }
//    }

//    public void broadcastFoxgloveLoggableOnChannel(FoxgloveLoggable obj, int channelId) {
//        String data = obj.toJson();
//        broadcastJsonMessageOnChannel(data, channelId);
//    }

//    public void advertiseFoxgloveLoggable(FoxgloveLoggable obj, int channelId) {
//        advertise(channelId, obj.path(), obj.toString(), obj.getSchema());
//    }

//    public void advertise(int id, String topic, String schemaName, String schema) {
//        JsonObject o = new JsonObject();
//        o.addProperty("op", "advertise");
//
//        JsonObject c = new JsonObject();
//        c.addProperty("id", id);
//        c.addProperty("topic", topic);
//        c.addProperty("encoding", "json");
//        c.addProperty("schemaName", schemaName);
//        c.addProperty("schemaEncoding", "jsonschema");
//        c.addProperty("schema", schema);
//
//        JsonArray channelsArray = new JsonArray();
//        channelsArray.add(c);
//        o.add("channels", channelsArray);
//        broadcast(GSON.toJson(o));
//    }

    public static ByteBuffer buildMessage(String msg, int subscriptionId) {
        Instant now = Instant.now();
        long epochNano = now.toEpochMilli() * 1_000_000L + (now.getNano() % 1_000_000L);
        byte[] payloadBytes = msg.getBytes(StandardCharsets.UTF_8);

        // op (1 byte) + id (4 bytes) + timestamp (8 bytes) + data
        ByteBuffer buffer = ByteBuffer.allocate(13 + payloadBytes.length)
                .order(ByteOrder.LITTLE_ENDIAN);

        buffer.put((byte) 0x01); // op
        buffer.putInt(subscriptionId); // id
        buffer.putLong(epochNano); // timestamp
        buffer.put(payloadBytes); // data
        buffer.flip();

        return buffer;
    }

//    public static List<FoxgloveChannel> unadvertisedChannels() {
//        return channels.stream().filter(channel -> !channel.hasAdvertised()).toList();
//    }

    public static String buildAdvertiseMessageString(int id, String topic, String schemaName, String schema) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "advertise");

        JsonObject c = new JsonObject();
        c.addProperty("id", id);
        c.addProperty("topic", topic);
        c.addProperty("encoding", "json");
        c.addProperty("schemaName", schemaName);
        c.addProperty("schemaEncoding", "jsonschema");
        c.addProperty("schema", schema);

        JsonArray channelsArray = new JsonArray();
        channelsArray.add(c);
        o.add("channels", channelsArray);

        return WEBSOCKET_GSON.toJson(o);
    }

    public static JsonObject buildBlankAdvertiseMessage() {
        JsonObject o = new JsonObject();
        o.addProperty("op", "advertise");

        JsonArray channelsArray = new JsonArray();
        o.add("channels", channelsArray);

        return o;
    }

    public static void addChannelToAdvertiseMessage(JsonObject advertiseMessage, FoxgloveChannel<? extends FoxgloveLoggable> channel) {
        JsonObject c = new JsonObject();
        FoxgloveLoggable loggable = channel.getLoggable();
        c.addProperty("id", channel.getId());
        c.addProperty("topic", channel.getTopic());
        c.addProperty("encoding", "json");
        c.addProperty("schemaName", loggable.getSchemaName());
        c.addProperty("schemaEncoding", "jsonschema");
        c.addProperty("schema", loggable.getSchema());

        advertiseMessage.getAsJsonArray("channels").add(c);
    }

//    public void runMultichannelAdvertisement(List<FoxgloveChannel> channels) {
//
//        if (channels.isEmpty()) return; // Skips blank lists
//
//        JsonObject o = buildBlankAdvertiseMessage(); // Creates the base for an advertisement message
//        channels.forEach(channel -> addChannelToAdvertiseMessage(o, channel)); // Adds each channel
//
//        broadcastJsonObject(o); // Broadcasts full message
//    }

    public void runFullAdvertisementCheck() {
//        runMultichannelAdvertisement(unadvertisedChannels());
        for (FoxgloveWebSocketConnection conn : connections) {
            conn.advertiseAllUnadvertisedChannels();
        }
    }

//    public void broadcastJsonObject(JsonObject jsonObject) {
//        broadcast(WEBSOCKET_GSON.toJson(jsonObject));
//    }

    public void broadcastChannelToAllConnections(FoxgloveChannel<? extends FoxgloveLoggable> channel) {
        for (FoxgloveWebSocketConnection conn : connections) {
            conn.broadcastChannel(channel);
        }
    }

//    public void advertise(int id, String topic) {
//        advertise(id, topic, "", "");
//    }

    public void freeChannel(FoxgloveChannel<? extends FoxgloveLoggable> channel) {
        channels.remove(channel);
    }
}
