package com.team3390.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.kauailabs.navx.frc.AHRS;
import com.team3390.lib.drivers.LazyTalonSRX;
import com.team3390.lib.drivers.TalonSRXCreator;
import com.team3390.lib.drivers.TalonSRXCreator.Configuration;
import com.team3390.robot.Constants;
import com.team3390.robot.Constants.LOWPOWERMODE_INCREASE_TYPE;
import com.team3390.robot.utility.CompetitionShuffleboard;
import com.team3390.robot.utility.LowPowerMode;
import com.team3390.robot.utility.PID;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
  
  private static DriveSubsystem instance; 
  private final CompetitionShuffleboard shuffleboard = CompetitionShuffleboard.getInstance();

  private boolean isBreakMode = false;

  private final Configuration talonConfiguration = new Configuration();
  private final LazyTalonSRX leftMaster, rightMaster, leftSlave1, rightSlave1, leftSlave2, rightSlave2;
  private final DifferentialDrive driveController;

  private final AHRS navX = new AHRS(Constants.SENSOR_NAVX_PORT);
  private final PID balancePID = new PID(
    Constants.DRIVE_BALANCE_PID_KP,
    Constants.DRIVE_BALANCE_PID_KI,
    Constants.DRIVE_BALANCE_PID_KD,
    Constants.DRIVE_BALANCE_PID_TOLERANCE,
    Constants.DRIVE_BALANCE_PID_MAXOUT,
    Constants.DRIVE_BALANCE_PID_MINOUT
  );

  public synchronized static DriveSubsystem getInstance() {
    if (instance == null) {
      instance = new DriveSubsystem();
    }
    return instance;
  }

  public DriveSubsystem() {    
    talonConfiguration.NEUTRAL_MODE = isBreakMode ? NeutralMode.Brake : NeutralMode.Coast;
    leftMaster = TalonSRXCreator.createTalon(Constants.DRIVE_LEFT_MASTER_ID, talonConfiguration);
    leftSlave1 = TalonSRXCreator.createCustomPermanentSlaveTalon(Constants.DRIVE_LEFT_SLAVE1_ID, Constants.DRIVE_LEFT_MASTER_ID, talonConfiguration);
    leftSlave2 = TalonSRXCreator.createCustomPermanentSlaveTalon(Constants.DRIVE_LEFT_SLAVE2_ID, Constants.DRIVE_LEFT_MASTER_ID, talonConfiguration);
    rightMaster = TalonSRXCreator.createTalon(Constants.DRIVE_RIGHT_MASTER_ID, talonConfiguration);
    rightSlave1 = TalonSRXCreator.createCustomPermanentSlaveTalon(Constants.DRIVE_RIGHT_SLAVE1_ID, Constants.DRIVE_RIGHT_MASTER_ID, talonConfiguration);
    rightSlave2 = TalonSRXCreator.createCustomPermanentSlaveTalon(Constants.DRIVE_RIGHT_SLAVE2_ID, Constants.DRIVE_RIGHT_MASTER_ID, talonConfiguration);

    leftMaster.setInverted(Constants.DRIVE_LEFT_INVERTED);
    leftSlave1.setInverted(Constants.DRIVE_LEFT_INVERTED);
    leftSlave2.setInverted(Constants.DRIVE_LEFT_INVERTED);
    rightMaster.setInverted(Constants.DRIVE_RIGHT_INVERTED);
    rightSlave1.setInverted(Constants.DRIVE_RIGHT_INVERTED);
    rightSlave2.setInverted(Constants.DRIVE_RIGHT_INVERTED);

    driveController = new DifferentialDrive(leftMaster, rightMaster);

    balancePID.setSetpoint(0);
  }

  @Override
  public void periodic() {
    shuffleboard.robotBalancedEntry.setBoolean(isBalanced());
    shuffleboard.robotLowPowerModeEntry.setBoolean(LowPowerMode.INSTANCE.getLowDriveModeEnabled());
    shuffleboard.robotRollEntry.setDouble(getRobotRoll());
    shuffleboard.robotHeadingEntry.setDouble(getHeading());
    
    SmartDashboard.putNumber("Balance PID out", balancePID.output(balancePID.calculate(getRobotRoll(), 0)));
  }

  public double getRobotRoll() {
    return navX.getRoll() + Constants.DRIVE_NAVX_ROLL_DEADBAND;
  }

  public double getHeading() {
    return Math.floor(navX.getAngle());
  }

  public boolean isBalanced() {
    balancePID.calculate(getRobotRoll(), 0);
    return balancePID.atSetpoint();
  }

  public void resetSensors() {
    navX.reset();
  }

  public void calibrateSensors() {
    navX.calibrate();
  }

  public void stopMotors() {
    driveController.stopMotor();
  }

  public boolean isBreakMode() {
    return isBreakMode;
  }

  public void setBrakeMode(boolean shouldEnable) {
    if (isBreakMode != shouldEnable) {
      isBreakMode = shouldEnable;
      NeutralMode mode = shouldEnable ? NeutralMode.Brake : NeutralMode.Coast;

      leftMaster.setNeutralMode(mode);
      leftSlave1.setNeutralMode(mode);
      leftSlave2.setNeutralMode(mode);

      rightMaster.setNeutralMode(mode);
      rightSlave1.setNeutralMode(mode);
      rightSlave2.setNeutralMode(mode);
    }
  }

  public void arcadeDrivePercent(double fwd, double rot) {
    if (LowPowerMode.INSTANCE.getLowDriveModeEnabled()) {
      fwd = LowPowerMode.INSTANCE.calculate(fwd, LOWPOWERMODE_INCREASE_TYPE.TREE);
      rot = LowPowerMode.INSTANCE.calculate(rot, LOWPOWERMODE_INCREASE_TYPE.TREE);
    }
    driveController.arcadeDrive(fwd, rot);
  }

  public void tankDrivePercent(double leftPercent, double rightPercent) {
    if (LowPowerMode.INSTANCE.getLowDriveModeEnabled()) {
      leftPercent = LowPowerMode.INSTANCE.calculate(leftPercent, LOWPOWERMODE_INCREASE_TYPE.TREE);
      rightPercent = LowPowerMode.INSTANCE.calculate(rightPercent, LOWPOWERMODE_INCREASE_TYPE.TREE);
    }
    driveController.tankDrive(leftPercent, rightPercent);
  }

  public void balanceRobot() {
    if (!balancePID.atSetpoint()) {
      double roll = Math.floor(navX.getRoll()) + Constants.DRIVE_NAVX_ROLL_DEADBAND;
      double calculatedSpeed = balancePID.output(balancePID.calculate(roll, 0));
      double rot = 0;
      if (navX.getAngle() > 0) {
        rot -= 0.3;
      } else if (navX.getAngle() < 0) {
        rot += 0.3;
      }
      driveController.arcadeDrive(calculatedSpeed, rot);
    } else {
      driveController.stopMotor();
    }
  }

  public void driveStraight(double fwd, double rot) {
    if (navX.getAngle() > 0) {
      rot -= 0.3;
    } else if (navX.getAngle() < 0) {
      rot += 0.3;
    }
    arcadeDrivePercent(fwd, rot);
  }

}
