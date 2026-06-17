package frc.robot.subsystems.drive;

import com.studica.frc.AHRS;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class GyroIONavX implements GyroIO {
  private final AHRS navx = new AHRS(AHRS.NavXComType.kMXP_SPI);

  @Override
  public void updateInputs(GyroIOInputs inputs) {
    inputs.connected = navx.isConnected();

    inputs.yawPosition = Rotation2d.fromDegrees(-navx.getAngle());
    inputs.yawVelocityRadPerSec = Units.degreesToRadians(-navx.getRawGyroZ());
  }
}
