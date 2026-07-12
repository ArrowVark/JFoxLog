package io.github.hudsoncrisp.jfoxlog.defaults.loggables;//package io.github.hudsoncrisp.jfoxlog.defaults.loggables;
//
//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.RobotBase;
//import edu.wpi.first.wpilibj.util.Color;
//import io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable.GenerateFoxgloveLoggable;
//import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
//import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketServer;
//
//import java.util.OptionalInt;
//
//public class Diagnostics {
//
//    private final DiagnosticsInputsFoxgloveLoggable inputs = new DiagnosticsInputsFoxgloveLoggable();
//
//    private final boolean isReal;
//    private Diagnostics(boolean isReal) {
//        this.isReal = isReal;
//    }
//
//    public static Diagnostics setup(boolean isReal) {
//        return new Diagnostics(isReal);
//    }
//
//    public void update() {
//        inputs.allianceColor = getAllianceColor();
//        inputs.onRedAlliance = onRedAlliance();
//        inputs.isRobotReal = isReal;
//        inputs.isFMSAttached = DriverStation.isFMSAttached();
//        inputs.isDSAttached = DriverStation.isDSAttached();
//
//        for (int i = 0; i <= DriverStation.kJoystickPorts - 1; i++) {
//            inputs.isJoystickConnected[i] = DriverStation.isJoystickConnected(i);
//        }
//
//        inputs.eventName = DriverStation.getEventName();
//        inputs.matchNumber = DriverStation.getMatchNumber();
//        inputs.matchType = DriverStation.getMatchType();
//        inputs.matchTime = DriverStation.getMatchTime();
//
//        OptionalInt stationLocation = DriverStation.getLocation();
//        if (stationLocation.isPresent()) {
//            inputs.stationLocation = stationLocation.getAsInt();
//        }
//
//        inputs.inAutonomous = DriverStation.isAutonomous();
//        inputs.isAutonomousEnabled = DriverStation.isAutonomousEnabled();
//        inputs.isTeleop = DriverStation.isTeleop();
//        inputs.isTeleopEnabled = DriverStation.isTeleopEnabled();
//        inputs.isTest = DriverStation.isTest();
//        inputs.isTestEnabled = DriverStation.isTestEnabled();
//        inputs.isEnabled = DriverStation.isEnabled();
//        inputs.isDisabled = DriverStation.isDisabled();
//        inputs.isEStopped = DriverStation.isEStopped();
//    }
//
//    private static Color getAllianceColor() {
//        var alliance = DriverStation.getAlliance();
//        Color allianceColor = Color.kGhostWhite;
//        if (alliance.isPresent())
//            allianceColor = alliance.get() == DriverStation.Alliance.Red
//                    ? Color.kRed
//                    : Color.kBlue;
//
//        return allianceColor;
//    }
//
//    private static boolean onRedAlliance() {
//        var alliance = DriverStation.getAlliance();
//
//        return alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red;
//    }
//
//    @GenerateFoxgloveLoggable(name="Diagnostics", parentPath="Robot/System")
//    public class DiagnosticsInputs {
//        public Color allianceColor = Diagnostics.getAllianceColor();
//        public boolean onRedAlliance = Diagnostics.onRedAlliance();
//        public boolean isRobotReal = isReal;
//        public boolean isFMSAttached = DriverStation.isFMSAttached();
//        public boolean isDSAttached = DriverStation.isDSAttached();
//        public boolean[] isJoystickConnected = {false, false, false, false, false, false}; // DriverStation.isJoystickConnected(i);
//        public String eventName = DriverStation.getEventName();
//        public int matchNumber = DriverStation.getMatchNumber();
//        public DriverStation.MatchType matchType = DriverStation.getMatchType();
//        public double matchTime = DriverStation.getMatchTime();
//        public int stationLocation = 0; // DriverStation.getLocation();
//        public boolean inAutonomous = DriverStation.isAutonomous();
//        public boolean isAutonomousEnabled = DriverStation.isAutonomousEnabled();
//        public boolean isTeleop = DriverStation.isTeleop();
//        public boolean isTeleopEnabled = DriverStation.isTeleopEnabled();
//        public boolean isTest = DriverStation.isTest();
//        public boolean isTestEnabled = DriverStation.isTestEnabled();
//        public boolean isEnabled = DriverStation.isEnabled();
//        public boolean isDisabled = DriverStation.isDisabled();
//        public boolean isEStopped = DriverStation.isEStopped();
//    }
//}
