/**
 * @author CanOkan, DenizOndes
 * @since 08-01-2023
 */

package com.team3390.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import com.team3390.robot.Constants.LIMELIGHT_LIGHT_MODE;
import com.team3390.robot.commands.autonomous.Pos4;
import com.team3390.robot.commands.drive.BalanceRobotCommand;
import com.team3390.robot.commands.drive.LockAprilTags;
import com.team3390.robot.commands.drive.LockRetroreflective;
import com.team3390.robot.commands.drive.TankDriveCommand;
import com.team3390.robot.commands.manuplators.ManuplatorMasterControl;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.LimelightSubsystem;
import com.team3390.robot.subsystems.ManuplatorSubsystem;
import com.team3390.robot.utility.LowPowerMode;

public class RobotContainer {

  private final LowPowerMode lowPowerMode = LowPowerMode.INSTANCE;

  private final DriveSubsystem driveSubsystem = DriveSubsystem.getInstance();
  private final LimelightSubsystem limelightSubsystem = LimelightSubsystem.getInstance();
  private final ManuplatorSubsystem manuplatorSubsystem = ManuplatorSubsystem.getInstance();

  private final Joystick leftStick = new Joystick(Constants.JOYSTICK_LEFT_PORT);
  private final Joystick rightStick = new Joystick(Constants.JOYSTICK_RIGHT_PORT);
  private final Joystick atari1 = new Joystick(Constants.JOYSTICK_ATARI1_PORT);
  // private final Joystick atari2 = new Joystick(Constants.JOYSTICK_ATARI2_PORT);
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
  
  public RobotContainer() {
    new Trigger(() -> rightStick.getRawButton(4)).onTrue(setLimelightForRetroreflectiveTape);
    new Trigger(() -> rightStick.getRawButton(5)).onTrue(setLimelightForAprilTags);
    new Trigger(() -> rightStick.getRawButton(2)).onTrue(lowPowerMode.toggleLowDriveModeCommand());

    new Trigger(() -> leftStick.getRawButton(6)).onTrue(resetSensorsCommand);
    
    new Trigger(() -> atari1.getRawButton(1)).whileTrue(lockRetroreflectiveCommand);
    new Trigger(() -> atari1.getRawButton(2)).whileTrue(lockAprilTagsCommand);
    new Trigger(() -> atari1.getRawButton(3)).whileTrue(balanceRobotCommand);

    driveSubsystem.resetSensors();

    driveSubsystem.setDefaultCommand(new TankDriveCommand(
      driveSubsystem,
      () -> leftStick.getY(),
      () -> rightStick.getY()
    ));
    manuplatorSubsystem.setDefaultCommand(
      new ManuplatorMasterControl(
        manuplatorSubsystem,
        () -> gamepad.getRawAxis(2),
        () -> gamepad.getRawAxis(4)
      )
    );
  }

  public Command getAutonomousCommand() {
    return new Pos4(driveSubsystem, true, false);
  }
}
