/**
 * @author CanOkan, DenizOndes
 * @since 08-01-2023
 */

package com.team3390.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import com.team3390.robot.Constants.LIMELIGHT_LIGHT_MODE;
import com.team3390.robot.commands.autonomous.Cone;
import com.team3390.robot.commands.autonomous.Cube;
import com.team3390.robot.commands.drive.BalanceRobotCommand;
import com.team3390.robot.commands.drive.LockAprilTags;
import com.team3390.robot.commands.drive.LockRetroreflective;
import com.team3390.robot.commands.drive.TankDriveCommand;
import com.team3390.robot.commands.manuplators.ManuplatorMasterControl;
import com.team3390.robot.commands.manuplators.BodyPosition;
import com.team3390.robot.commands.manuplators.ExtractCone;
import com.team3390.robot.commands.manuplators.ExtractCube;
import com.team3390.robot.commands.manuplators.IntakeCone;
import com.team3390.robot.commands.manuplators.IntakeCube;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.LimelightSubsystem;
import com.team3390.robot.subsystems.ManuplatorSubsystem;
import com.team3390.robot.utility.LowPowerMode;

public class RobotContainer {

  private final LowPowerMode lowPowerMode = LowPowerMode.INSTANCE;

  private final Compressor comp = new Compressor(PneumaticsModuleType.CTREPCM);

  private final DriveSubsystem driveSubsystem = DriveSubsystem.getInstance();
  private final LimelightSubsystem limelightSubsystem = LimelightSubsystem.getInstance();
  private final ManuplatorSubsystem manuplatorSubsystem = ManuplatorSubsystem.getInstance();

  private final Joystick leftStick = new Joystick(Constants.JOYSTICK_LEFT_PORT);
  private final Joystick rightStick = new Joystick(Constants.JOYSTICK_RIGHT_PORT);
  private final Joystick gamepad = new Joystick(Constants.JOYSTICK_GAMEPAD_PORT);

  private final BalanceRobotCommand balanceRobotCommand = new BalanceRobotCommand(driveSubsystem, true);
  private final ResetSensorsCommand resetSensorsCommand = new ResetSensorsCommand(driveSubsystem);
  private final LockRetroreflective lockRetroreflectiveCommand = new LockRetroreflective(driveSubsystem);
  private final LockAprilTags lockAprilTagsCommand = new LockAprilTags(driveSubsystem);

  private final Command setLimelightForRetroreflectiveTape = new InstantCommand(() -> {
    limelightSubsystem.setLedMode(LIMELIGHT_LIGHT_MODE.ON);
    limelightSubsystem.setPipeline(0);
  });
  private final Command setLimelightForAprilTags = new InstantCommand(() -> {
    limelightSubsystem.setLedMode(LIMELIGHT_LIGHT_MODE.PIPELINE_VALUE);
    limelightSubsystem.setPipeline(1);
  });

  private final Command compOff = new InstantCommand(() -> {
    comp.disable();
  });
  private final Command compOn = new InstantCommand(() -> {
    comp.enableDigital();
  });

  private final SendableChooser<Integer> autoChooser = new SendableChooser<>();
  private final SendableChooser<Boolean> balanceChooser = new SendableChooser<>();
  private final SendableChooser<Boolean> alwaysBalanceChooser = new SendableChooser<>();
  private Command sellectedAuto;
  
  public RobotContainer() {
    CameraServer.startAutomaticCapture();

    new Trigger(() -> rightStick.getRawButton(4)).onTrue(setLimelightForRetroreflectiveTape);
    new Trigger(() -> rightStick.getRawButton(5)).onTrue(setLimelightForAprilTags);
    new Trigger(() -> rightStick.getRawButton(1)).onTrue(lowPowerMode.toggleLowDriveModeCommand());

    new Trigger(() -> leftStick.getRawButton(6)).onTrue(resetSensorsCommand);
    new Trigger(() -> leftStick.getRawButton(4)).onTrue(compOff);
    new Trigger(() -> leftStick.getRawButton(5)).onTrue(compOn);
    
    new Trigger(() -> gamepad.getRawButton(1)).whileTrue(lockRetroreflectiveCommand);
    new Trigger(() -> gamepad.getRawButton(3)).whileTrue(lockAprilTagsCommand);
    // new Trigger(() -> gamepad.getRawButton(6)).whileTrue(balanceRobotCommand);

    new Trigger(() -> gamepad.getRawButton(5)).whileTrue(new IntakeCube(manuplatorSubsystem));
    new Trigger(() -> gamepad.getRawButton(6)).whileTrue(new ExtractCube(manuplatorSubsystem));
    new Trigger(() -> gamepad.getRawButton(7)).whileTrue(new IntakeCone(manuplatorSubsystem));
    new Trigger(() -> gamepad.getRawButton(8)).whileTrue(new ExtractCone(manuplatorSubsystem));

    new Trigger(() -> gamepad.getRawButton(4)).onTrue(new BodyPosition(manuplatorSubsystem, () -> -1.0, () -> 3.75));

    driveSubsystem.resetSensors();

    driveSubsystem.setDefaultCommand(new TankDriveCommand(
      driveSubsystem,
      () -> leftStick.getY(),
      () -> rightStick.getY()
    ));
    manuplatorSubsystem.setDefaultCommand(
      new ManuplatorMasterControl(
        manuplatorSubsystem,
        () -> gamepad.getRawAxis(3),
        () -> gamepad.getRawAxis(1)
      )
    );

    autoChooser.addOption("Pos 1 (Cone)", 1);
    autoChooser.addOption("Pos 2 (Cube)", 2);
    autoChooser.addOption("Pos 3 (Cone)", 3);
    autoChooser.addOption("Pos 4 (Cone)", 4);
    autoChooser.setDefaultOption("Pos 5 (Cube)", 5);
    autoChooser.addOption("Pos 6 (Cone)", 6);
    autoChooser.addOption("Pos 7 (Cone)", 7);
    autoChooser.addOption("Pos 8 (Cube)", 8);
    autoChooser.addOption("Pos 9 (Cone)", 9);

    balanceChooser.setDefaultOption("Yes (Balance)", true);
    balanceChooser.addOption("No", false);

    alwaysBalanceChooser.setDefaultOption("Yes (Always)", true);
    alwaysBalanceChooser.addOption("No", false);

    SmartDashboard.putData("Auto Mode", autoChooser);
    SmartDashboard.putData("Balance", balanceChooser);
    SmartDashboard.putData("Always Balance", alwaysBalanceChooser);
  }

  public Command getAutonomousCommand() {
    boolean balance = false;
    int sellected = autoChooser.getSelected();

    if (sellected == 4 || sellected == 5 || sellected == 6)
      balance = balanceChooser.getSelected();

    if (sellected == 1 || sellected == 3 || sellected == 5 || sellected == 7 || sellected == 9)
      sellectedAuto = new Cone(driveSubsystem, balance, alwaysBalanceChooser.getSelected());
    else
      sellectedAuto = new Cube(driveSubsystem, balance, alwaysBalanceChooser.getSelected());

    return sellectedAuto;
  }
}
