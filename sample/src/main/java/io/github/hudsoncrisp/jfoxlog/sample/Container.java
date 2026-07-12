package io.github.hudsoncrisp.jfoxlog.sample;

public class Container {
//    public static DriverDashboard Dashboard;
//    public static OperatorInterface OperatorInterface;
//    public static SendableChooser<Command> AutoChooser;
//
//    // public static PwmLEDs LEDs;
//    public static Swerve Swerve;
//    public static LimelightVision LimelightVision;
//    public static PhotonVision PhotonVision;
//    public static Pneumatics Pneumatics;
//    public static Hopper Hopper;
//    public static Turret Turret;
//    public static PowerDistribution PD;

    public static void initialize() {
//        try {
//            OperatorInterface = new OperatorInterface();
//            // Create dashboard
//            Dashboard = new DriverDashboard();
//
//            // Create subsystems
//            // LEDs = new PwmLEDs();
//
//            LimelightVision = new LimelightVision();
//            LimelightVision.addCamera(VisionMap.LimelightTurretName, VisionMap.LimelightTurretTransform);
//            PhotonVision = new PhotonVision();
//            PhotonVision.addCamera(VisionMap.PhotonCam1Name, VisionMap.PhotonCam1Transform);
//            PhotonVision.addCamera(VisionMap.PhotonCam2Name, VisionMap.PhotonCam2Transform);
//
//            Swerve = new Swerve();
//            Pneumatics = new Pneumatics();
//            Hopper = new Hopper();
//            Turret = new Turret();
//            PD = new PowerDistribution(1, ModuleType.kRev);
//
//            // Create and bind the operator interface
//            OperatorInterface.bindDriverControls();
//            OperatorInterface.bindOperatorControls();
//
//            // Register the named commands from each subsystem that may be used in PathPlanner
//            NamedCommands.registerCommands(getNamedCommandSuppliers());
//
//            // Build an auto chooser. This will use Commands.none() as the default option.
//            AutoChooser = AutoBuilder.buildAutoChooser();
//            Dashboard.putData("Auto Chooser", AutoChooser);
//        } catch (Exception e) {
//            DriverStation.reportError("[ERROR] >> Failed to initialize Container: " + e.getMessage(), e.getStackTrace());
//        }
    }

//    public static List<Pair<String, Command>> getNamedCommandSuppliers() {
//        return List.of(
//                // Pair.of("Disable_Autonomous_Shooting", toggleShooterOff()),
//                // Pair.of("Start_Auto", startAuto()),
//                Pair.of("Take_Out_And_Enable_Intake", toggleIntakeOn()),
//                Pair.of("Put_In_And_Disable_Intake", toggleIntakeOff()),
//                Pair.of("Dump_Balls", ejectBalls()));
//    }
}
