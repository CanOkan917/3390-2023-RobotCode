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
import frc.robot.commands.utility.ResetSensorsCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utility.LowPowerMode;

public class RobotContainer {

  private final LowPowerMode lowPowerMode = LowPowerMode.INSTANCE;

  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  // private final LimelightSubsystem limelightSubsystem = LimelightSubsystem.INSTANCE;

  private final Joystick leftStick = new Joystick(Constants.JOYSTICK_LEFT_PORT);
  private final Joystick rightStick = new Joystick(Constants.JOYSTICK_RIGHT_PORT);
  // private final Joystick atari1 = new Joystick(Constants.JOYSTICK_ATARI1_PORT);
  // private final Joystick atari2 = new Joystick(Constants.JOYSTICK_ATARI2_PORT);

  private final BalanceRobotCommand balanceRobotCommand = new BalanceRobotCommand(driveSubsystem);
  private final ResetSensorsCommand resetSensorsCommand = new ResetSensorsCommand(driveSubsystem);
  
  public RobotContainer() {
    configureBindings();

    driveSubsystem.setDefaultCommand(new DefaultCommand(driveSubsystem, leftStick, rightStick));
  }

  private void configureBindings() {
    // new Trigger(() -> leftStick.getRawButton(2)).onTrue(limelightSubsystem.setCamModeCommand(LIMELIGHT_CAMERA_MODE.DRIVE));
    // new Trigger(() -> leftStick.getRawButton(4)).onTrue(limelightSubsystem.setCamModeCommand(LIMELIGHT_CAMERA_MODE.VISION));

    new Trigger(() -> rightStick.getRawButton(1)).whileTrue(balanceRobotCommand);
    new Trigger(() -> rightStick.getRawButton(2)).onTrue(lowPowerMode.toggleLowDriveModeCommand());

    new Trigger(() -> leftStick.getRawButton(12)).onTrue(resetSensorsCommand);
    // new Trigger(() -> );
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
