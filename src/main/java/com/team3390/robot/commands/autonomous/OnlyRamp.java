package com.team3390.robot.commands.autonomous;

import com.team3390.robot.commands.drive.DriveStraight;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class OnlyRamp extends SequentialCommandGroup {
  public OnlyRamp(DriveSubsystem driveSubsystem) {
    addCommands(
      new ResetSensorsCommand(driveSubsystem),
      new ParallelDeadlineGroup(
        new WaitCommand(2),
        new DriveStraight(driveSubsystem, 0.8)
      )
    );
  }
}
