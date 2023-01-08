package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.TankDriveCommand;
import frc.robot.subsystems.DriveSubsystem;

public class DefaultCommand extends SequentialCommandGroup {

  public DefaultCommand(DriveSubsystem driveSubsystem, Joystick left, Joystick right, Joystick atari1, Joystick atari2) {
    addCommands(
      new ParallelCommandGroup(
        new TankDriveCommand(driveSubsystem, left, right)
      )
    );
  }

}
