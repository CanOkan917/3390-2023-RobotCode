package com.team3390.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.team3390.lib.drivers.LazyTalonSRX;
import com.team3390.lib.drivers.TalonSRXCreator;
import com.team3390.robot.Constants;
import com.team3390.robot.Constants.LOWPOWERMODE_INCREASE_TYPE;
import com.team3390.robot.utility.CompetitionShuffleboard;
import com.team3390.robot.utility.LowPowerMode;
import com.team3390.robot.utility.PID;

public class DriveSubsystem extends SubsystemBase {
  
  private static DriveSubsystem instance; 
  private final CompetitionShuffleboard shuffleboard = CompetitionShuffleboard.INSTANCE;

  private boolean isBreakMode;

  private final LazyTalonSRX leftMaster, rightMaster, leftSlave, rightSlave;
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

  private final LimelightSubsystem limelight = LimelightSubsystem.getInstance();

  public synchronized static DriveSubsystem getInstance() {
    if (instance == null) {
      instance = new DriveSubsystem();
    }
    return instance;
  }

  public DriveSubsystem() {    
    leftMaster = TalonSRXCreator.createDefaultMasterTalon(Constants.DRIVE_LEFT_MASTER_ID);
    leftSlave = TalonSRXCreator.createDefaultPermanentSlaveTalon(Constants.DRIVE_LEFT_SLAVE_ID, Constants.DRIVE_LEFT_MASTER_ID);
    rightMaster = TalonSRXCreator.createDefaultMasterTalon(Constants.DRIVE_RIGHT_MASTER_ID);
    rightSlave = TalonSRXCreator.createDefaultPermanentSlaveTalon(Constants.DRIVE_RIGHT_SLAVE_ID, Constants.DRIVE_RIGHT_MASTER_ID);

    leftMaster.setInverted(Constants.DRIVE_LEFT_INVERTED);
    leftSlave.setInverted(Constants.DRIVE_LEFT_INVERTED);
    rightMaster.setInverted(Constants.DRIVE_RIGHT_INVERTED);
    rightSlave.setInverted(Constants.DRIVE_RIGHT_INVERTED);

    isBreakMode = false;

    driveController = new DifferentialDrive(leftMaster, rightMaster);

    balancePID.setSetpoint(0);
  }

  @Override
  public void periodic() {
    shuffleboard.robotBalancedEntry.setBoolean(isBalanced());
    shuffleboard.robotLowPowerModeEntry.setBoolean(LowPowerMode.INSTANCE.getLowDriveModeEnabled());
    SmartDashboard.putNumber("nivix", getRobotRoll());
  }

  public double getRobotRoll() {
    return Math.floor(navX.getRoll()) + Constants.DRIVE_NAVX_ROLL_DEADBAND;
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
      leftSlave.setNeutralMode(mode);

      rightMaster.setNeutralMode(mode);
      rightSlave.setNeutralMode(mode);
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
      double calculatedSpeed = -balancePID.output(balancePID.calculate(roll, 0));
      driveController.arcadeDrive(calculatedSpeed, 0);
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

  public void lockTarget() {
    if (limelight.isTarget() && !limelight.atSetpoint()) {
      double xSpeed = -limelight.getXOutput();
      double ySpeed = -limelight.getYOutput();
      SmartDashboard.putNumber("X", xSpeed);
      SmartDashboard.putNumber("Y", ySpeed);
      arcadeDrivePercent(ySpeed, xSpeed / 1.5);
    }
  }
}
