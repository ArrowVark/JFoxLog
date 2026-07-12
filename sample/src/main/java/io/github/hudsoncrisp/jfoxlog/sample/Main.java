package io.github.hudsoncrisp.jfoxlog.sample;

import io.github.hudsoncrisp.jfoxlog.JFoxLog;
import io.github.hudsoncrisp.jfoxlog.debug.FoxgloveDebugPanel;

import javax.swing.*;

public class Main {

    private Main() {}

    public static void main(String... args) {
//        RobotBase.startRobot(Robot::new);
        FoxgloveDebugPanel.show();
        Timer timer = new Timer(5, e -> {
            JFoxLog.periodic();
        });
        timer.setInitialDelay(1);
        timer.start();

//        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//            System.out.println("Theme Name: " + info.getName());
//            System.out.println("Class Name: " + info.getClassName());
//            System.out.println("----------------------------------------------");
//        }
    }
}
