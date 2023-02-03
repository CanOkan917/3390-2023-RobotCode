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

  private boolean isBreakMode = true;

  private final LazyTalonSRX bodyMaster, handMaster, handIntake;

  private final Configuration bodyConfiguration = new Configuration();
  private final Configuration handConfiguration = new Configuration();
  private final Configuration intakeConfiguration = new Configuration();

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
    handConfiguration.NEUTRAL_MODE = isBreakMode ? NeutralMode.Brake : NeutralMode.Coast;
    handConfiguration.INVERTED = Constants.ELEVATOR_HAND_INVERTED;
    intakeConfiguration.INVERTED = Constants.ELEVATOR_INTAKE_INVERTED;

    bodyMaster = TalonSRXCreator.createTalon(Constants.ELEVATOR_BODY_ID, bodyConfiguration);
    handMaster = TalonSRXCreator.createTalon(Constants.ELEVATOR_HAND_ID, handConfiguration);
    handIntake = TalonSRXCreator.createTalon(Constants.ELEVATOR_INTAKE_ID, intakeConfiguration);

    topSwitch = new DigitalInput(Constants.ELEVATOR_TOP_SWITCH_PORT);
    bottomSwtich = new DigitalInput(Constants.ELEVATOR_BOTTOM_SWITCH_PORT);

    intakeSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.ELEVATOR_INTAKE_SOLENOID_PORT);
  }

  @Override
  public void periodic() {
    shuffleboard.eTopSwitchEnabledEntry.setBoolean(topSwitch.get());
    shuffleboard.eBottomSwitchEnabledEntry.setBoolean(bottomSwtich.get());
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

      handMaster.setNeutralMode(mode);
    }
  }

  public void setIntakeBrakeMode(boolean shouldEnable) {
    if (isBreakMode != shouldEnable) {
      isBreakMode = shouldEnable;
      NeutralMode mode = shouldEnable ? NeutralMode.Brake : NeutralMode.Coast;

      handIntake.setNeutralMode(mode);
    }
  }

  public void body(double speed) {
    if (topSwitch.get() || bottomSwtich.get())
      bodyMaster.set(0);
    else
      bodyMaster.set(speed);
  }

  public void hand(double speed) {
    handMaster.set(speed);
  }

  public class cone {
    public void init() {
      intakeSolenoid.set(true);
    }
    public void execute_intake() {
      handIntake.set(0.8);
    }
    public void execute_extract() {
      intakeSolenoid.set(false);
    }
    public void end() {
      handIntake.set(0);
    }
  }

  public class cube {
    public void init() {
      intakeSolenoid.set(false);
    }
    public void execute_intake() {
      handIntake.set(0.8);
    }
    public void execute_extract() {
      handIntake.set(-0.8);
    }
    public void end() {
      handIntake.set(0);
    }
  }

}
