package frc.robot.subsystems.drive;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import org.littletonrobotics.junction.Logger;
import edu.wpi.first.math.filter.Debouncer;

public class Module {
  private final ModuleIO io;
  private final ModuleIOInputsAutoLogged inputs = new ModuleIOInputsAutoLogged();
  private final int index;

  private final SimpleMotorFeedforward ffModel =
      new SimpleMotorFeedforward(DriveConstants.driveKs, DriveConstants.driveKv);


  private final Debouncer driveConnectedDebouncer =
      new Debouncer(0.5, Debouncer.DebounceType.kFalling);
  private final Debouncer turnConnectedDebouncer =
      new Debouncer(0.5, Debouncer.DebounceType.kFalling);
  private final Debouncer encoderConnectedDebouncer =
      new Debouncer(0.5, Debouncer.DebounceType.kFalling);

  private final Alert driveDisconnectedAlert;
  private final Alert turnDisconnectedAlert;
  private final Alert encoderDisconnectedAlert;

  private SwerveModulePosition[] odometryPositions = new SwerveModulePosition[] {};

  public Module(ModuleIO io, int index) {
    this.io = io;
    this.index = index;

    io.setDrivePID(DriveConstants.driveKp, 0.0, DriveConstants.driveKd);
    io.setTurnPID(DriveConstants.turnKp, 0.0, DriveConstants.turnKd);

    driveDisconnectedAlert =
        new Alert("Disconnected drive motor on module " + index + ".", AlertType.kError);
    turnDisconnectedAlert =
        new Alert("Disconnected turn motor on module " + index + ".", AlertType.kError);
    encoderDisconnectedAlert =
        new Alert("Disconnected turn encoder on module " + index + ".", AlertType.kError);
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Drive/Module" + index, inputs);

    int sampleCount = inputs.odometryDrivePositionsRad.length;
    odometryPositions = new SwerveModulePosition[sampleCount];
    for (int i = 0; i < sampleCount; i++) {
      double positionMeters =
          inputs.odometryDrivePositionsRad[i] * DriveConstants.wheelRadiusMeters;
      Rotation2d angle = inputs.odometryTurnPositions[i];
      odometryPositions[i] = new SwerveModulePosition(positionMeters, angle);
    }

    driveDisconnectedAlert.set(!driveConnectedDebouncer.calculate(inputs.driveConnected));
    turnDisconnectedAlert.set(!turnConnectedDebouncer.calculate(inputs.turnConnected));
    encoderDisconnectedAlert.set(!encoderConnectedDebouncer.calculate(inputs.encoderConnected));
  }

  public void runSetpoint(SwerveModuleState state) {
    state.optimize(getAngle());
    state.cosineScale(getAngle());

    double speedRadPerSec = state.speedMetersPerSecond / DriveConstants.wheelRadiusMeters;
    io.runDriveVelocity(speedRadPerSec, ffModel.calculate(speedRadPerSec));
    io.runTurnPosition(state.angle);
  }

  public void runCharacterization(double output) {
    io.runDriveOpenLoop(output);
    io.runTurnPosition(Rotation2d.kZero);
  }

  public void stop() {
    io.runDriveOpenLoop(0.0);
    io.runTurnOpenLoop(0.0);
  }

  public Rotation2d getAngle() {
    return inputs.turnPositionRads;
  }

  public double getPositionMeters() {
    return inputs.drivePositionRads * DriveConstants.wheelRadiusMeters;
  }

  public double getVelocityMetersPerSec() {
    return inputs.driveVelocityRadsPerSec * DriveConstants.wheelRadiusMeters;
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(getPositionMeters(), getAngle());
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(getVelocityMetersPerSec(), getAngle());
  }

  public SwerveModulePosition[] getOdometryPositions() {
    return odometryPositions;
  }

  public double getWheelRadiusCharacterizationPosition() {
    return inputs.drivePositionRads;
  }

  public double getFFCharacterizationVelocity() {
    return inputs.driveVelocityRadsPerSec;
  }

  public void setBrakeMode(boolean enabled) {
    io.setBrakeMode(enabled);
  }
}
