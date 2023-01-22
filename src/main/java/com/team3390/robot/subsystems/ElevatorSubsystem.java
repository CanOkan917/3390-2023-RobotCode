package com.team3390.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team3390.lib.drivers.LazyTalonSRX;
import com.team3390.lib.drivers.TalonSRXCreator;
import com.team3390.lib.drivers.TalonSRXCreator.Configuration;
import com.team3390.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {

  private static ElevatorSubsystem instance;

  private boolean isBreakMode = true;

  private final LazyTalonSRX bodyMaster, bodySlave;
  private final Configuration bodyConfiguration = new Configuration();

  public synchronized static ElevatorSubsystem getInstance() {
    if (instance == null) {
      instance = new ElevatorSubsystem();
    }
    return instance;
  }

  public ElevatorSubsystem() {
    bodyConfiguration.NEUTRAL_MODE = isBreakMode ? NeutralMode.Brake : NeutralMode.Coast;
    bodyMaster = TalonSRXCreator.createTalon(Constants.ELEVATOR_BODY_MASTER_PORT, bodyConfiguration);
    bodySlave = TalonSRXCreator.createCustomPermanentSlaveTalon(Constants.ELEVATOR_BODY_SLAVE_PORT, Constants.ELEVATOR_BODY_MASTER_PORT, bodyConfiguration);
  }

  @Override
  public void periodic() {
  }

  public void setBrakeMode(boolean shouldEnable) {
    if (isBreakMode != shouldEnable) {
      isBreakMode = shouldEnable;
      NeutralMode mode = shouldEnable ? NeutralMode.Brake : NeutralMode.Coast;

      bodyMaster.setNeutralMode(mode);
      bodySlave.setNeutralMode(mode);
    }
  }
}
