package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.DriveConstants;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class DriveCommands {
  private static final double DEADBAND = 0.1;

  private DriveCommands() {}

  private static Translation2d getLinearVelocityFromJoysticks(double x, double y) {
    double magnitude = MathUtil.applyDeadband(Math.hypot(x, y), DEADBAND);
    Rotation2d direction = new Rotation2d(x, y);
    magnitude = magnitude * magnitude;
    return new Translation2d(magnitude, direction);
  }

  private static double getOmegaFromJoysticks(double omega) {
    double deadbanded = MathUtil.applyDeadband(omega, DEADBAND);
    return deadbanded * deadbanded * Math.signum(deadbanded);
  }

  public static Command joystickDrive(
      Drive drive,
      DoubleSupplier xSupplier,
      DoubleSupplier ySupplier,
      DoubleSupplier omegaSupplier,
      BooleanSupplier robotRelative) {
    return Commands.run(
        () -> {
          Translation2d linear =
              getLinearVelocityFromJoysticks(xSupplier.getAsDouble(), ySupplier.getAsDouble());
          double omega = getOmegaFromJoysticks(omegaSupplier.getAsDouble());

          ChassisSpeeds speeds =
              new ChassisSpeeds(
                  linear.getX() * DriveConstants.maxSpeedMetersPerSec,
                  linear.getY() * DriveConstants.maxSpeedMetersPerSec,
                  omega * DriveConstants.maxSpeedMetersPerSec);

          drive.runVelocity(
              robotRelative.getAsBoolean()
                  ? speeds
                  : ChassisSpeeds.fromFieldRelativeSpeeds(speeds, drive.getRotation()));
        },
        drive);
  }
}
