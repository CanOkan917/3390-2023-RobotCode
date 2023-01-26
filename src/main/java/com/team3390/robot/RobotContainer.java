/**
 * @author CanOkan, DenizOndes
 * @since 08-01-2023
 */

package com.team3390.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import com.team3390.robot.Constants.LIMELIGHT_LIGHT_MODE;
import com.team3390.robot.commands.autonomous.Pos4;
import com.team3390.robot.commands.drive.BalanceRobotCommand;
import com.team3390.robot.commands.drive.LockRetroreflective;
import com.team3390.robot.commands.drive.TankDriveCommand;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.LimelightSubsystem;
import com.team3390.robot.utility.LowPowerMode;

public class RobotContainer {

  private final LowPowerMode lowPowerMode = LowPowerMode.INSTANCE;

  private final DriveSubsystem driveSubsystem = DriveSubsystem.getInstance();
  private final LimelightSubsystem limelightSubsystem = LimelightSubsystem.getInstance();

  private final Joystick leftStick = new Joystick(Constants.JOYSTICK_LEFT_PORT);
  private final Joystick rightStick = new Joystick(Constants.JOYSTICK_RIGHT_PORT);
  private final Joystick atari1 = new Joystick(Constants.JOYSTICK_ATARI1_PORT);
  // private final Joystick atari2 = new Joystick(Constants.JOYSTICK_ATARI2_PORT);

  private final BalanceRobotCommand balanceRobotCommand = new BalanceRobotCommand(driveSubsystem, true);
  private final ResetSensorsCommand resetSensorsCommand = new ResetSensorsCommand(driveSubsystem);
  private final LockRetroreflective lockTargetCommand = new LockRetroreflective(driveSubsystem);
  
  public RobotContainer() {
    new Trigger(() -> rightStick.getRawButton(4)).onTrue(limelightSubsystem.setLedModeCommand(LIMELIGHT_LIGHT_MODE.OFF));
    new Trigger(() -> rightStick.getRawButton(5)).onTrue(limelightSubsystem.setLedModeCommand(LIMELIGHT_LIGHT_MODE.ON));
    new Trigger(() -> rightStick.getRawButton(1)).whileTrue(balanceRobotCommand);
    new Trigger(() -> rightStick.getRawButton(2)).onTrue(lowPowerMode.toggleLowDriveModeCommand());

    new Trigger(() -> leftStick.getRawButton(4)).onTrue(limelightSubsystem.setPipelineCommand(0));
    new Trigger(() -> leftStick.getRawButton(5)).onTrue(limelightSubsystem.setPipelineCommand(1));
    new Trigger(() -> leftStick.getRawButton(6)).onTrue(resetSensorsCommand);
    new Trigger(() -> leftStick.getRawButton(1)).whileTrue(lockTargetCommand);
    
    new Trigger(() -> atari1.getRawButton(1)).whileTrue(lockTargetCommand);

    driveSubsystem.resetSensors();

    driveSubsystem.setDefaultCommand(new TankDriveCommand(
      driveSubsystem,
      () -> leftStick.getY(),
      () -> rightStick.getY()
    ));
  }

  public Command getAutonomousCommand() {
    return new Pos4(driveSubsystem, true, true);
  }
}
