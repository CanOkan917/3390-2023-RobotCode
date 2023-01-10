/**
 * @author CanOkan, DenizOndes
 * @since 08-01-2023
 */

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DefaultCommand;
import frc.robot.commands.drive.BalanceRobotCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utility.LowPowerMode;

public class RobotContainer {

  private final DriveSubsystem driveSubsystem = new DriveSubsystem();

  private final Joystick leftStick = new Joystick(Constants.JOYSTICK_LEFT_PORT);
  private final Joystick rightStick = new Joystick(Constants.JOYSTICK_RIGHT_PORT);
  private final Joystick atari1 = new Joystick(Constants.JOYSTICK_ATARI1_PORT);
  private final Joystick atari2 = new Joystick(Constants.JOYSTICK_ATARI2_PORT);

  private final BalanceRobotCommand balanceRobotCommand = new BalanceRobotCommand(driveSubsystem);
  
  public RobotContainer() {
    configureBindings();

    driveSubsystem.setDefaultCommand(new DefaultCommand(driveSubsystem, leftStick, rightStick, atari1));
  }

  private void configureBindings() {
    new Trigger(() -> atari2.getRawButton(1)).whileTrue(balanceRobotCommand);
    new Trigger(() -> atari2.getRawButton(7)).onTrue(LowPowerMode.INSTANCE.toggleLowDriveModeCommand());

    new Trigger(() -> atari1.getRawButton(12)).whileTrue(balanceRobotCommand);
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
