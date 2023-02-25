package com.team3390.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team3390.lib.drivers.LazyTalonSRX;
import com.team3390.lib.drivers.TalonSRXCreator;
import com.team3390.lib.drivers.TalonSRXCreator.Configuration;
import com.team3390.robot.Constants;
import com.team3390.robot.utility.CompetitionShuffleboard;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ManuplatorSubsystem extends SubsystemBase {

  private static ManuplatorSubsystem instance;
  private final CompetitionShuffleboard shuffleboard = CompetitionShuffleboard.getInstance();

  private boolean isBreakMode = false;

  private final LazyTalonSRX bodyMaster, elbowMaster, handMaster;

  private final Configuration bodyConfiguration = new Configuration();
  private final Configuration elbowConfiguration = new Configuration();
  private final Configuration handConfiguration = new Configuration();

  private final DigitalInput topSwitch, bottomSwtich;

  private final Solenoid intakeSolenoid;

  public synchronized static ManuplatorSubsystem getInstance() {
    if (instance == null) {
      instance = new ManuplatorSubsystem();
    }
    return instance;
  }

  public ManuplatorSubsystem() {
    bodyConfiguration.NEUTRAL_MODE = isBreakMode ? NeutralMode.Brake : NeutralMode.Coast;
    bodyConfiguration.INVERTED = Constants.ELEVATOR_BODY_INVERTED;
    elbowConfiguration.NEUTRAL_MODE = isBreakMode ? NeutralMode.Brake : NeutralMode.Coast;
    elbowConfiguration.INVERTED = Constants.ELEVATOR_ELBOW_INVERTED;
    handConfiguration.INVERTED = Constants.ELEVATOR_HAND_INVERTED;

    bodyMaster = TalonSRXCreator.createTalon(Constants.ELEVATOR_BODY_ID, bodyConfiguration);
    elbowMaster = TalonSRXCreator.createTalon(Constants.ELEVATOR_ELBOW_ID, elbowConfiguration);
    handMaster = TalonSRXCreator.createTalon(Constants.ELEVATOR_HAND_ID, handConfiguration);

    topSwitch = new DigitalInput(Constants.ELEVATOR_TOP_SWITCH_PORT);
    bottomSwtich = new DigitalInput(Constants.ELEVATOR_BOTTOM_SWITCH_PORT);

    intakeSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.ELEVATOR_INTAKE_SOLENOID_PORT);
  }

  @Override
  public void periodic() {
    shuffleboard.eTopSwitchEnabledEntry.setBoolean(isBodyOnTop());
    shuffleboard.eBottomSwitchEnabledEntry.setBoolean(isBodyOnBottom());
  }

  public void setBodyBrakeMode(boolean shouldEnable) {
    if (isBreakMode != shouldEnable) {
      isBreakMode = shouldEnable;
      NeutralMode mode = shouldEnable ? NeutralMode.Brake : NeutralMode.Coast;

      bodyMaster.setNeutralMode(mode);
    }
  }

  public void setHandBrakeMode(boolean shouldEnable) {
    if (isBreakMode != shouldEnable) {
      isBreakMode = shouldEnable;
      NeutralMode mode = shouldEnable ? NeutralMode.Brake : NeutralMode.Coast;

      elbowMaster.setNeutralMode(mode);
    }
  }

  public void setIntakeBrakeMode(boolean shouldEnable) {
    if (isBreakMode != shouldEnable) {
      isBreakMode = shouldEnable;
      NeutralMode mode = shouldEnable ? NeutralMode.Brake : NeutralMode.Coast;

      handMaster.setNeutralMode(mode);
    }
  }

  public boolean isBodyOnTop() {
    return Constants.ELEVATOR_TOP_SWITCH_INVERTED ? !topSwitch.get() : topSwitch.get();
  }

  public boolean isBodyOnBottom() {
    return Constants.ELEVATOR_BOTTOM_SWITCH_INVERTED ? !bottomSwtich.get() : bottomSwtich.get();
  }

  public void bodyWithSwitches(double speed) {
    if (topSwitch.get() || bottomSwtich.get())
      bodyMaster.set(0);
    else
      bodyMaster.set(speed);
  }

  public void body(double speed) {
    bodyMaster.set(speed);
  }

  public void elbow(double speed) {
    elbowMaster.set(speed);
  }

  public void cone_init() {
    intakeSolenoid.set(true);
  }
  public void cone_execute_intake() {
    handMaster.set(1);
  }
  public void cone_execute_extract() {
    intakeSolenoid.set(false);
  }
  public void cone_end() {
    handMaster.set(0);
  }

  public void cube_init() {
    intakeSolenoid.set(false);
  }
  public void cube_execute_intake() {
    handMaster.set(1);
  }
  public void cube_execute_extract() {
    handMaster.set(-1);
  }
  public void cube_end() {
    handMaster.set(0);
  }

}
