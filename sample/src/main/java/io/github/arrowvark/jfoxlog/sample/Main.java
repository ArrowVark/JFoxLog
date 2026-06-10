package io.github.arrowvark.jfoxlog.sample;

import edu.wpi.first.wpilibj.RobotBase;

public class Main {

    private Main() {}

    public static void main(String... args) {
        RobotBase.startRobot(Robot::new);
    }
}
