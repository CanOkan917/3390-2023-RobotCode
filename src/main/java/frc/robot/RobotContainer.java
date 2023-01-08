/**
 * @author CanOkan, DenizOndes
 * @since 08-01-2023
 */

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DefaultCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utility.LowPowerMode;

public class RobotContainer {

  private final DriveSubsystem driveSubsystem = new DriveSubsystem();

  private final Joystick leftStick = new Joystick(Constants.JOYSTICK_LEFT_PORT);
  private final Joystick rightStick = new Joystick(Constants.JOYSTICK_RIGHT_PORT);
  private final Joystick atari1 = new Joystick(Constants.JOYSTICK_ATARI1_PORT);
  private final Joystick atari2 = new Joystick(Constants.JOYSTICK_ATARI2_PORT);
  
  public RobotContainer() {
    configureBindings();

    driveSubsystem.setDefaultCommand(new DefaultCommand(driveSubsystem, leftStick, rightStick, atari1, atari2));
  }

  private void configureBindings() {
    new Trigger(() -> atari2.getRawButtonPressed(0)).onTrue(LowPowerMode.INSTANCE.toggleLowDriveModeCommand());
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
