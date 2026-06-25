package frc.robot.subsystems.drive;

import frc.robot.Constants;

public final class DriveConstants {
  public static final double turnReduction = 287.0 / 11.0; // 26.09:1
  public static final double driveReduction = 6.1224;
  public static final double wheelRadiusMeters = 0.0508;
  public static final double driveSimInertia = 0.025;
  public static final double turnSimInertia = 0.004;
  public static final double driveKp;
  public static final double driveKd;
  public static final double driveKs;
  public static final double driveKv;
  public static final double turnKp;
  public static final double turnKd;

  static {
    switch (Constants.getMode()) {
      case REAL -> {
        driveKp = 0.0;
        driveKd = 0.0;
        driveKs = 0.0;
        driveKv = 0.0;
        turnKp = 0.0;
        turnKd = 0.0;
      }
      default -> { // SIM, REPLAY
        driveKp = 0.1;
        driveKd = 0.0;
        driveKs = 0.014;
        driveKv = 0.134;
        turnKp = 10.0;
        turnKd = 0.0;
      }
    }
  }
}
