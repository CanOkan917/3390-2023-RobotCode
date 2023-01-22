package com.team3390.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.team3390.robot.Constants;
import com.team3390.robot.Constants.LOWPOWERMODE_INCREASE_TYPE;
import com.team3390.robot.utility.CompetitionShuffleboard;
import com.team3390.robot.utility.LowPowerMode;
import com.team3390.robot.utility.PID;
import com.team3390.robot.utility.drivers.LazyTalonSRX;
import com.team3390.robot.utility.drivers.TalonSRXCreator;

public class DriveSubsystem extends SubsystemBase {

  private final double mMotorDeadband = 0.02;
  private final double mMotorMaxOut = 1.0;
  
  private boolean isBreakMode;

  private final CompetitionShuffleboard shuffleboard = CompetitionShuffleboard.INSTANCE;

  private final LazyTalonSRX leftMaster, rightMaster, leftSlave, rightSlave;

  private final AHRS navX = new AHRS(Constants.SENSOR_NAVX_PORT);
  private final PID balancePID = new PID(
    Constants.DRIVE_BALANCE_PID_KP,
    Constants.DRIVE_BALANCE_PID_KI,
    Constants.DRIVE_BALANCE_PID_KD,
    Constants.DRIVE_BALANCE_PID_TOLERANCE,
    Constants.DRIVE_BALANCE_PID_MAXOUT,
    Constants.DRIVE_BALANCE_PID_MINOUT
  );

  public DriveSubsystem() {    
    leftMaster = TalonSRXCreator.createDefaultMasterTalon(Constants.DRIVE_LEFT_MASTER_ID);
    leftSlave = TalonSRXCreator.createDefaultPermanentSlaveTalon(Constants.DRIVE_LEFT_SLAVE_ID, Constants.DRIVE_LEFT_MASTER_ID);
    rightMaster = TalonSRXCreator.createDefaultMasterTalon(Constants.DRIVE_RIGHT_MASTER_ID);
    rightSlave = TalonSRXCreator.createDefaultPermanentSlaveTalon(Constants.DRIVE_RIGHT_SLAVE_ID, Constants.DRIVE_RIGHT_MASTER_ID);

    isBreakMode = true;

    balancePID.setSetpoint(0);
  }

  @Override
  public void periodic() {
    shuffleboard.robotBalancedEntry.setBoolean(balancePID.atSetpoint());
    shuffleboard.robotLowPowerModeEntry.setBoolean(LowPowerMode.INSTANCE.getLowDriveModeEnabled());
    shuffleboard.robotPitch.setDouble(Math.floor(navX.getRoll()) + Constants.DRIVE_NAVX_ROLL_DEADBAND);
    shuffleboard.robotBalancePIDOutput.setDouble(balancePID.output(balancePID.calculate(Math.floor(navX.getRoll()) + Constants.DRIVE_NAVX_ROLL_DEADBAND, 0)));
  }

  /**
   * Bütün sürüş ile alakalı sensörleri sıfırlar
   */
  public void resetSensors() {
    navX.reset();
  }

  /**
   * Bütün sürüş ile alakalı sensörleri kalibre eder
   * 
   * Robot ilk açıldığında kalibrasyon yapmak önemlidir!
   */
  public void calibrateSensors() {
    navX.calibrate();
  }

  /**
   * Bütün motorları durdurur.
   */
  public void stopMotors() {
    leftMaster.stopMotor();
    rightMaster.stopMotor();
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

  /**
   * Robotun arcade tarzda sürülmesini sağlar
   * @param fwd X ekseninde hız (%)
   * @param rot Y ekseninde hız (%)
   */
  public void arcadeDrive(double fwd, double rot) {
    if (LowPowerMode.INSTANCE.getLowDriveModeEnabled()) {
      fwd = LowPowerMode.INSTANCE.calculate(fwd, LOWPOWERMODE_INCREASE_TYPE.TREE);
      rot = LowPowerMode.INSTANCE.calculate(rot, LOWPOWERMODE_INCREASE_TYPE.TREE);
    }
    this.arcadeDrive(fwd, rot, false);
  }

  /**
   * Robotun tank tarzında sürülmesini sağlar
   * @param leftPercent Sol tekereklere gidecek hız (%)
   * @param rightPercent Sağ tekereklere gidecek hız (%)
   */
  public void tankDrivePercent(double leftPercent, double rightPercent) {
    if (LowPowerMode.INSTANCE.getLowDriveModeEnabled()) {
      leftPercent = LowPowerMode.INSTANCE.calculate(leftPercent, LOWPOWERMODE_INCREASE_TYPE.TREE);
      rightPercent = LowPowerMode.INSTANCE.calculate(rightPercent, LOWPOWERMODE_INCREASE_TYPE.TREE);
    }
    this.tankDrive(leftPercent, rightPercent, false);
  }

  /**
   * Robotun rampanın üzerinde dengede durabilmesi için hazırlanmış
   * PID kontrolcüsü eşliğinde çalışan hizzalama bölümü
   */
  public void balanceRobot() {
    if (!balancePID.atSetpoint()) {
      double roll = Math.floor(navX.getRoll()) + Constants.DRIVE_NAVX_ROLL_DEADBAND;
      double calculatedSpeed = balancePID.output(balancePID.calculate(roll, 0));
      this.arcadeDrive(calculatedSpeed, 0, false);
    } else {
      this.stopMotors();
    }
  }

  private void arcadeDrive(double fwd, double rot, boolean squareInputs) {
    fwd = MathUtil.applyDeadband(fwd, mMotorDeadband);
    rot = MathUtil.applyDeadband(rot, mMotorDeadband);
    fwd = MathUtil.clamp(fwd, -1.0, -1.0);
    rot = MathUtil.clamp(rot, -1.0, -1.0);

    if (squareInputs) {
      fwd = Math.copySign(fwd * fwd, fwd);
      rot = Math.copySign(rot * rot, rot);
    }

    double leftSpeed = fwd - rot;
    double rightSpeed = fwd + rot;

    double greaterInput = Math.max(Math.abs(fwd), Math.abs(rot));
    double lesserInput = Math.min(Math.abs(fwd), Math.abs(rot));
    if (greaterInput == 0.0) {
      leftMaster.set(0.0 * mMotorMaxOut);
      rightMaster.set(0.0 * mMotorMaxOut);
    }
    double saturatedInput = (greaterInput + lesserInput) / greaterInput;
    leftSpeed /= saturatedInput;
    rightSpeed /= saturatedInput;

    leftMaster.set(leftSpeed * mMotorMaxOut);
    rightMaster.set(rightSpeed * mMotorMaxOut);
  }

  private void tankDrive(double left, double right, boolean squareInputs) {
    left = MathUtil.applyDeadband(left, mMotorDeadband);
    right = MathUtil.applyDeadband(right, mMotorDeadband);
    left = MathUtil.clamp(left, -1.0, -1.0);
    right = MathUtil.clamp(right, -1.0, -1.0);

    if (squareInputs) {
      left = Math.copySign(left * left, left);
      right = Math.copySign(right * right, right);
    }

    leftMaster.set(left * mMotorMaxOut);
    rightMaster.set(right * mMotorMaxOut);
  }
}
