package io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FoxgloveWebSocketConnection {
    private final WebSocket connection;
    private final Map<Integer, Integer> channelIdToSubscriptionId = new ConcurrentHashMap<>();
    private final Map<FoxgloveChannel<? extends FoxgloveLoggable>, Boolean> channels = new ConcurrentHashMap<>(); // Channels and if they have advertised
    private final Map<Integer, FoxgloveChannel<? extends FoxgloveLoggable>> channelIdToChannel = new ConcurrentHashMap<>();

    private final AtomicInteger lowerFrequencyCounter = new AtomicInteger(100);

    public FoxgloveWebSocketConnection(WebSocket connection, List<FoxgloveChannel<? extends FoxgloveLoggable>> channels) {
        this.connection = connection;
        channels.forEach(channel -> {
            this.channels.putIfAbsent(channel, false);
            this.channelIdToChannel.putIfAbsent(channel.getId(), channel);
        });
    }

    public void broadcast(String msg) {
        connection.send(msg);
    }

    public void subscribe(int channelId, int subscriptionId) {
        channelIdToSubscriptionId.putIfAbsent(channelId, subscriptionId);

        FoxgloveChannel<?> channel = channelIdToChannel.get(channelId);

        if (channels.get(channel) && channel.getLoggingType() == FoxgloveChannel.LoggingType.LOW_FREQUENCY_SERVER_DRIVEN) {
            connection.send(
                    FoxgloveWebSocketServer.buildMessage(channel.requestData(), subscriptionId)
            );
        } else if (channels.get(channel) && channel.getLoggingType() == FoxgloveChannel.LoggingType.LOW_FREQUENCY_SERVER_DRIVEN_STATIC) {
            connection.send(
                    FoxgloveWebSocketServer.buildMessage(channel.requestCache(), subscriptionId)
            );
        }
    }

    public void unsubscribe(int subscriptionId) {
        channelIdToSubscriptionId.forEach((channelId, subId) -> {
            if (subId == subscriptionId) {
                channelIdToSubscriptionId.remove(channelId, subId);
            }
        });
    }

    public void advertiseAllUnadvertisedChannels() {
        List<FoxgloveChannel<? extends FoxgloveLoggable>> unadvertised = unadvertisedChannels();
        if (unadvertised.isEmpty()) return;

        JsonObject o = FoxgloveWebSocketServer.buildBlankAdvertiseMessage();
        for (FoxgloveChannel<? extends FoxgloveLoggable> channel : unadvertised) {
            FoxgloveWebSocketServer.addChannelToAdvertiseMessage(o, channel);
            channels.replace(channel, true);
        }

        connection.send(FoxgloveWebSocketServer.WEBSOCKET_GSON.toJson(o));
        System.out.println("Advertise All Unadvertised Channels: " + FoxgloveWebSocketServer.WEBSOCKET_GSON.toJson(o));
    }

    public void broadcastAllServerDrivenChannels() {

        // Check if we should log the lower frequency topics too
        boolean logAll = lowerFrequencyCounter.decrementAndGet() <= 0;

        if (logAll) {
            lowerFrequencyCounter.set(100);
        }

        // Pass 1: Data Pass
        Map<FoxgloveChannel<? extends FoxgloveLoggable>, String> dataCache = new HashMap<>();
        channels.forEach((channel, hasAdvertised) -> {
            boolean baseCase = hasAdvertised && channel.getLoggingType() == FoxgloveChannel.LoggingType.SERVER_DRIVEN;
            boolean dataCase = baseCase || (logAll && channel.getLoggingType() == FoxgloveChannel.LoggingType.LOW_FREQUENCY_SERVER_DRIVEN);
            boolean cacheCase = baseCase || (logAll && channel.getLoggingType() == FoxgloveChannel.LoggingType.LOW_FREQUENCY_SERVER_DRIVEN_STATIC);

            if (dataCase) {
                dataCache.put(channel, channel.requestData());
            } else if (cacheCase) {
                dataCache.put(channel, channel.requestCache());
            }
        });

        // Pass 2: Broadcast Pass
        dataCache.forEach((channel, data) -> {
            Integer subscriptionId = subscriptionIdFromChannelId(channel.getId());
            if (subscriptionId == null) return;

            connection.send(
                    FoxgloveWebSocketServer.buildMessage(data, subscriptionId)
            );

            System.out.println("Broadcast All Server Driven Channels: " + new String(FoxgloveWebSocketServer.buildMessage(data, subscriptionId).array(), StandardCharsets.UTF_8));
        });
    }

    public void broadcastChannel(FoxgloveChannel<? extends FoxgloveLoggable> channel) {
        boolean hasAdvertised = channels.get(channel);
        if (hasAdvertised) {
            Integer subscriptionId = subscriptionIdFromChannelId(channel.getId());
            if (subscriptionId == null) return;

            connection.send(
                    FoxgloveWebSocketServer.buildMessage(
                            channel.requestData(),
                            subscriptionId
                    )
            );

            System.out.println("Broadcast Channel: " + FoxgloveWebSocketServer.buildMessage(
                    channel.requestData(),
                    subscriptionId
            ));
        }
    }

    public void addChannel(FoxgloveChannel<? extends FoxgloveLoggable> channel) {
        this.channels.putIfAbsent(channel, false);
    }

    public void broadcastServerInfo() {
        connection.send(FoxgloveWebSocketServer.SERVER_INFO_STRING);
        System.out.println("Broadcast Server Info: " + FoxgloveWebSocketServer.SERVER_INFO_STRING);
    }

    public List<FoxgloveChannel<? extends FoxgloveLoggable>> unadvertisedChannels() {
        List<FoxgloveChannel<? extends FoxgloveLoggable>> unadvertisedChannels = new ArrayList<>();
        channels.forEach((channel, hasAdvertised) -> {
            if (!hasAdvertised) {
                unadvertisedChannels.add(channel);
            }
        });

        return unadvertisedChannels;
    }

    public Integer subscriptionIdFromChannelId(int channelId) {
        return channelIdToSubscriptionId.get(channelId);
    }

    public boolean webSocketMatchesConnection(WebSocket webSocket) {
        return webSocket == connection;
    }
}
