package frc.robot.subsystems.drive;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants;

public final class DriveConstants {

  public static final double TrackWidthXMeters = 0.53975;
  public static final double TrackWidthYMeters = 0.53975;

  public static final double maxSpeedMetersPerSec = 4;
  public static final double maxAngularSpeedRadPerSec = 2 * Math.PI;

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

  public static final Translation2d[] moduleTranslations =
      new Translation2d[] {
        new Translation2d(TrackWidthXMeters / 2.0, TrackWidthYMeters / 2.0), // FL
        new Translation2d(TrackWidthXMeters / 2.0, -TrackWidthYMeters / 2.0), // FR
        new Translation2d(-TrackWidthXMeters / 2.0, TrackWidthYMeters / 2.0), // BL
        new Translation2d(-TrackWidthXMeters / 2.0, -TrackWidthYMeters / 2.0) // BR
      };

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
