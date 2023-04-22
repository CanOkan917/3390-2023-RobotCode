/**
 * @author CanOkan, DenizOndes
 * @since 08-01-2023
 */

package com.team3390.robot;

import com.team3390.lib.sensors.PressureSensor;
import com.team3390.robot.Constants.LIMELIGHT_LIGHT_MODE;
import com.team3390.robot.commands.autonomous.Cone;
import com.team3390.robot.commands.autonomous.Cube;
import com.team3390.robot.commands.autonomous.OnlyRamp;
import com.team3390.robot.commands.autonomous.Taxi;
import com.team3390.robot.commands.drive.BalanceRobotCommand;
import com.team3390.robot.commands.drive.TankDriveCommand;
import com.team3390.robot.commands.manuplators.ExtractCone;
import com.team3390.robot.commands.manuplators.ExtractCube;
import com.team3390.robot.commands.manuplators.IntakeCone;
import com.team3390.robot.commands.manuplators.IntakeCube;
import com.team3390.robot.commands.manuplators.ManuplatorMasterControl;
import com.team3390.robot.commands.manuplators.auto.Hand3rdLevel2;
import com.team3390.robot.commands.manuplators.auto.HandFloorLevel2;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.LimelightSubsystem;
import com.team3390.robot.subsystems.ManuplatorSubsystem;
import com.team3390.robot.utility.CompetitionShuffleboard;
import com.team3390.robot.utility.LowPowerMode;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {

  private final LowPowerMode lowPowerMode = LowPowerMode.INSTANCE;
  private final CompetitionShuffleboard shuffleboard = CompetitionShuffleboard.getInstance();

  private final Compressor comp = new Compressor(PneumaticsModuleType.CTREPCM);
  private final PressureSensor pressureSensor = new PressureSensor(Constants.SENSOR_PRESSURE_PORT);

  private final DriveSubsystem driveSubsystem = DriveSubsystem.getInstance();
  private final LimelightSubsystem limelightSubsystem = LimelightSubsystem.getInstance();
  private final ManuplatorSubsystem manuplatorSubsystem = ManuplatorSubsystem.getInstance();

  private final Joystick leftStick = new Joystick(Constants.JOYSTICK_LEFT_PORT);
  private final Joystick rightStick = new Joystick(Constants.JOYSTICK_RIGHT_PORT);
  private final Joystick gamepad = new Joystick(Constants.JOYSTICK_GAMEPAD_PORT);

  private final Command balanceRobotCommand = new BalanceRobotCommand(driveSubsystem);
  private final Command resetSensorsCommand = new ResetSensorsCommand(driveSubsystem);
  private final Command intakeCube = new IntakeCube(manuplatorSubsystem);
  private final Command intakeCone = new IntakeCone(manuplatorSubsystem);
  private final Command extractCube = new ExtractCube(manuplatorSubsystem);
  private final Command extractCone = new ExtractCone(manuplatorSubsystem);
  private final Command hand3rdLevel = new Hand3rdLevel2(manuplatorSubsystem);
  private final Command handFloorLevel = new HandFloorLevel2(manuplatorSubsystem);

  private final Command setLimelightForRetroreflectiveTape = new InstantCommand(() -> {
    limelightSubsystem.setLedMode(LIMELIGHT_LIGHT_MODE.ON);
    limelightSubsystem.setPipeline(0);
  });
  private final Command setLimelightForAprilTags = new InstantCommand(() -> {
    limelightSubsystem.setLedMode(LIMELIGHT_LIGHT_MODE.PIPELINE_VALUE);
    limelightSubsystem.setPipeline(1);
  });
  private final Command setLimelightForCamera = new InstantCommand(() -> limelightSubsystem.setPipeline(2));

  private final Command compOff = new InstantCommand(() -> comp.disable());
  private final Command compOn = new InstantCommand(() -> comp.enableDigital());

  private final SendableChooser<Integer> autoModeChooser = new SendableChooser<>();
  private final SendableChooser<Boolean> balanceChooser = new SendableChooser<>();
  private final SendableChooser<Boolean> taxiChooser = new SendableChooser<>();
  private Command sellectedAuto;
  
  public RobotContainer() {
    new Thread(() -> {
      UsbCamera handCam = CameraServer.startAutomaticCapture("Hand Camera", "/dev/video0");
      handCam.setResolution(640, 480);

      if (Thread.interrupted()) {
        CameraServer.removeCamera("Hand Camera");
        DriverStation.reportError("Camera ERROR!!! Disabled camera", false);  
      }
    }).start();

    new Trigger(() -> rightStick.getRawButton(1)).onTrue(lowPowerMode.toggleLowDriveModeCommand());
    new Trigger(() -> rightStick.getRawButton(4)).onTrue(setLimelightForRetroreflectiveTape);
    new Trigger(() -> rightStick.getRawButton(5)).onTrue(setLimelightForAprilTags);
    new Trigger(() -> rightStick.getRawButton(6)).onTrue(setLimelightForCamera);

    new Trigger(() -> leftStick.getRawButton(1)).whileTrue(balanceRobotCommand);
    new Trigger(() -> leftStick.getRawButton(4)).onTrue(compOff);
    new Trigger(() -> leftStick.getRawButton(5)).onTrue(compOn);
    new Trigger(() -> leftStick.getRawButton(6)).onTrue(resetSensorsCommand);

    new Trigger(() -> gamepad.getRawButton(4)).onTrue(hand3rdLevel);
    new Trigger(() -> gamepad.getRawButton(2)).onTrue(handFloorLevel);
    new Trigger(() -> gamepad.getRawButton(3)).whileTrue(manuplatorSubsystem.arm_stable_command());
    new Trigger(() -> gamepad.getRawButton(12)).whileTrue(manuplatorSubsystem.arm_stable_command());
    new Trigger(() -> gamepad.getRawButton(11)).onTrue(manuplatorSubsystem.resetGyro());

    new Trigger(() -> gamepad.getRawButton(5)).whileTrue(intakeCube);
    new Trigger(() -> gamepad.getRawButton(6)).whileTrue(extractCube);
    new Trigger(() -> gamepad.getRawButton(7)).whileTrue(intakeCone);
    new Trigger(() -> gamepad.getRawButton(8)).whileTrue(extractCone);

    driveSubsystem.calibrateSensors();
    driveSubsystem.resetSensors();

    limelightSubsystem.setPipeline(0);

    driveSubsystem.setDefaultCommand(
      new TankDriveCommand(
        driveSubsystem,
        () -> leftStick.getY(),
        () -> rightStick.getY()
      )
    );
    manuplatorSubsystem.setDefaultCommand(
      new ManuplatorMasterControl(
        manuplatorSubsystem,
        () -> gamepad.getRawAxis(3),
        () -> gamepad.getRawAxis(1)
      )
    );

    autoModeChooser.addOption("Cone", 1);
    autoModeChooser.addOption("Cube", 2);
    autoModeChooser.addOption("Taxi", 3);
    autoModeChooser.setDefaultOption("Ramp", 4);

    balanceChooser.addOption("Yes (Balance)", true);
    balanceChooser.setDefaultOption("No", false);

    taxiChooser.setDefaultOption("Yes (Taxi)", true);
    taxiChooser.addOption("No", false);

    SmartDashboard.putData("Auto Mode", autoModeChooser);
    SmartDashboard.putData("Balance", balanceChooser);
    SmartDashboard.putData("Taxi", taxiChooser);
  }

  public void updateVars() {
    shuffleboard.robotPressureEntry.setDouble(pressureSensor.getPressure());
  }

  public Command getAutonomousCommand() {
    int sellected = autoModeChooser.getSelected();
    limelightSubsystem.setPipeline(0);

    switch (sellected) {
      case 1:
        sellectedAuto = new Cone(driveSubsystem, manuplatorSubsystem, balanceChooser.getSelected(), taxiChooser.getSelected());
        break;

      case 2:
        sellectedAuto = new Cube(driveSubsystem, manuplatorSubsystem, balanceChooser.getSelected(), taxiChooser.getSelected());
        break;

      case 3:
        sellectedAuto = new Taxi(driveSubsystem);
        break;

      default:
        sellectedAuto = new OnlyRamp(driveSubsystem);
    }      

    manuplatorSubsystem.resetSensors();
    driveSubsystem.resetSensors();

    return sellectedAuto;
  }
}
