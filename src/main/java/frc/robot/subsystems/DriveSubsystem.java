package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.LOWPOWERMODE_INCREASE_TYPE;
import frc.robot.utility.CompetitionShuffleboard;
import frc.robot.utility.LowPowerMode;
import frc.robot.utility.PID;

public class DriveSubsystem extends SubsystemBase {
  
  private final ShuffleboardTab shuffleboard = CompetitionShuffleboard.INSTANCE.tab;
  private final GenericEntry robotBalancedEntry = shuffleboard.add("Balanced", false).getEntry();
  private final GenericEntry robotLowPowerModeEntry = shuffleboard.add("LowPowerMode-DriveMotors Enabled", false).getEntry();
  private final GenericEntry robotPitch = shuffleboard.add("Roll", 0).getEntry();
  private final GenericEntry robotBalancePIDOutput = shuffleboard.add("BalancePID-OUTPUT", 0).getEntry();

  private final WPI_TalonSRX leftLeader  = new WPI_TalonSRX(Constants.DRIVE_LEFT_LEADER_ID);
  private final WPI_TalonSRX leftMotor2  = new WPI_TalonSRX(Constants.DRIVE_LEFT_MOTOR2_ID);
  private final WPI_TalonSRX rightLeader = new WPI_TalonSRX(Constants.DRIVE_RIGHT_LEADER_ID);
  private final WPI_TalonSRX rightMotor2 = new WPI_TalonSRX(Constants.DRIVE_RIGHT_MOTOR2_ID);

  private final DifferentialDrive drive = new DifferentialDrive(leftLeader, rightLeader);

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
    leftLeader.setInverted(Constants.DRIVE_LEFT_INVERTED);
    leftMotor2.setInverted(Constants.DRIVE_LEFT_INVERTED);
    rightLeader.setInverted(Constants.DRIVE_RIGHT_INVERTED);
    rightMotor2.setInverted(Constants.DRIVE_RIGHT_INVERTED);

    leftMotor2.follow(leftLeader);
    rightMotor2.follow(rightLeader);
    
    resetSensors();

    balancePID.setSetpoint(0);
  }

  @Override
  public void periodic() {
    robotBalancedEntry.setBoolean(balancePID.atSetpoint());
    robotLowPowerModeEntry.setBoolean(LowPowerMode.INSTANCE.getLowDriveModeEnabled());
    robotPitch.setDouble(Math.floor(navX.getRoll()) + Constants.DRIVE_NAVX_ROLL_DEADBAND);
    robotBalancePIDOutput.setDouble(balancePID.output(balancePID.calculate(Math.floor(navX.getRoll()) + Constants.DRIVE_NAVX_ROLL_DEADBAND, 0)));
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
    drive.stopMotor();
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
    drive.arcadeDrive(fwd, rot);
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
    drive.tankDrive(leftPercent, rightPercent);
  }

  /**
   * Robotun rampanın üzerinde dengede durabilmesi için hazırlanmış
   * PID kontrolcüsü eşliğinde çalışan hizzalama bölümü
   */
  public void balanceRobot() {
    if (!balancePID.atSetpoint()) {
      double roll = Math.floor(navX.getRoll()) + Constants.DRIVE_NAVX_ROLL_DEADBAND;
      double calculatedSpeed = balancePID.output(balancePID.calculate(roll, 0));
      drive.arcadeDrive(calculatedSpeed, 0);
    } else {
      drive.stopMotor();
    }
  }
}
