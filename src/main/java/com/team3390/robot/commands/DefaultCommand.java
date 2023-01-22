package com.team3390.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import com.team3390.robot.commands.drive.TankDriveCommand;
import com.team3390.robot.subsystems.DriveSubsystem;

public class DefaultCommand extends SequentialCommandGroup {

  public DefaultCommand(DriveSubsystem driveSubsystem, Joystick left, Joystick right) {
    addCommands(
      new ParallelCommandGroup(
        new TankDriveCommand(driveSubsystem, left, right)
      )
    );
  }

}
