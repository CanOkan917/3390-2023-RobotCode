package com.team3390.robot.commands.autonomous;

import com.team3390.robot.commands.drive.DriveStraight;
import com.team3390.robot.commands.manuplators.ExtractCube;
import com.team3390.robot.commands.manuplators.IntakeCube;
import com.team3390.robot.commands.manuplators.auto.Hand3rdLevel;
import com.team3390.robot.commands.manuplators.auto.HandFloorLevel;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Cube extends SequentialCommandGroup {

  public Cube(DriveSubsystem driveSubsystem, ManuplatorSubsystem manuplatorSubsystem, boolean balance) {
    manuplatorSubsystem.bodyGyro.reset();
    addCommands(
      new ResetSensorsCommand(driveSubsystem),
      new ParallelDeadlineGroup(
        new WaitCommand(0.1),
        new IntakeCube(manuplatorSubsystem)
      ),
      new Hand3rdLevel(manuplatorSubsystem),
      new ParallelDeadlineGroup(
        new WaitCommand(0.2),
        new ExtractCube(manuplatorSubsystem)
      ),
      new ParallelCommandGroup(
        new ParallelDeadlineGroup(
          new WaitCommand(1),
          new DriveStraight(driveSubsystem, 0.7)  
        ),
        new HandFloorLevel(manuplatorSubsystem)
      )
    );

    if (balance) {
      addCommands(
        new ParallelCommandGroup(
          new HandFloorLevel(manuplatorSubsystem),
          new OnlyRamp(driveSubsystem)
        )
      );
    }
  }
}
