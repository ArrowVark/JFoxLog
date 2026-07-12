package io.github.arrowvark.jfoxlog.sample;

import io.github.arrowvark.jfoxlog.foxglove.servers.http.FoxgloveHttpServer;
import io.github.arrowvark.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketServer;
import org.littletonrobotics.junction.LoggedRobot;

import edu.wpi.first.net.WebServer;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;

public class Robot extends LoggedRobot {

    public static EventLoop EventLoop = new EventLoop();
    private Command _autonomousCommand;

    private FoxgloveWebSocketServer _foxgloveServer;
    private FoxgloveHttpServer _foxgloveHttpServer;

    public Robot() {
        _foxgloveServer = FoxgloveWebSocketServer.getInstance();
        _foxgloveHttpServer = FoxgloveHttpServer.getInstance();
//
//        // Set up pathfinding compatibility with AdvantageKit
//        Pathfinding.setPathfinder(new LocalADStarADK());
//
//        // Configure logging
//        DriverStation.silenceJoystickConnectionWarning(true);

        // Initialize the robot container
        Container.initialize();

        // Set up the dashboard
//        WebServer.start(5800, Filesystem.getDeployDirectory().getPath()); // Start the web server for downloading elastic layout from robot
//        Elastic.selectTab("Auto");
//        Preferences.removeAll();
    }

    @Override
    public void disabledInit() {
        DataLogManager.log("Robot disabled");
//        CommandScheduler.getInstance().schedule(Container.Swerve.disableAutoAlignCommand());
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * things that you want ran during all modes.
     */
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        EventLoop.poll();
//        Diagnostics.update();
        _foxgloveServer.periodic();
    }

    /**
     * This function is called once each time the robot enters Autonomous mode.
     */
    @Override
    public void autonomousInit() {
        // Cancel any auto command that's still running
        if (_autonomousCommand != null)
            _autonomousCommand.cancel();

        // _autonomousCommand = Container.AutoChooser.getSelected();

        if (_autonomousCommand == null || _autonomousCommand == Commands.none()) {
            DriverStation.reportError("[ERROR] >> No auto command selected", false);
        } else {
            // Reset the gyro immediately if we're on the red alliance to get the correct angle (reversed on red alliance)
//            if (onRedAlliance()) {
//                Container.Swerve.resetGyro();
//            }

            // Schedule the auto command
            CommandScheduler.getInstance().schedule(_autonomousCommand);
        }
    }

    /**
     * This function is called once each time the robot enters Teleop mode.
     */
    @Override
    public void teleopInit() {
        DataLogManager.log("Teleop Enabled");

        if (_autonomousCommand != null) {
            // Cancel the auto command if it's still running
            _autonomousCommand.cancel();
        }
    }

    /**
     * This function is called once each time the robot enters Test mode.
     */
    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    public static boolean onRedAlliance() {
        var alliance = DriverStation.getAlliance();

        return alliance.isPresent() && alliance.get() == Alliance.Red;
    }

    public static boolean onBlueAlliance() {
        var alliance = DriverStation.getAlliance();

        return alliance.isPresent() && alliance.get() == Alliance.Blue;
    }

    public static Color getAllianceColor() {
        var alliance = DriverStation.getAlliance();
        Color allianceColor = Color.kGhostWhite;
        if (alliance.isPresent())
            allianceColor = alliance.get() == Alliance.Red
                    ? Color.kRed
                    : Color.kBlue;

        return allianceColor;
    }
}
