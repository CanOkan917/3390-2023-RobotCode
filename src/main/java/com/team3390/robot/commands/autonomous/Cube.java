package com.team3390.robot.commands.autonomous;

import com.team3390.robot.commands.drive.DriveStraight;
import com.team3390.robot.commands.manuplators.ExtractCube;
import com.team3390.robot.commands.manuplators.auto.Hand3rdLevel2;
import com.team3390.robot.commands.manuplators.auto.HandFloorLevel2;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Cube extends SequentialCommandGroup {

  public Cube(DriveSubsystem driveSubsystem, ManuplatorSubsystem manuplatorSubsystem, boolean balance, boolean taxi) {
    manuplatorSubsystem.bodyGyro.reset();
    addCommands(
      new ResetSensorsCommand(driveSubsystem),
      new Hand3rdLevel2(manuplatorSubsystem),
      new ParallelDeadlineGroup(
        new WaitCommand(1),
        new DriveStraight(driveSubsystem, -0.6)  
      ),
      new ParallelDeadlineGroup(
        new WaitCommand(0.2),
        new ExtractCube(manuplatorSubsystem)
      )
    );

    if (balance) {
      addCommands(
        new ParallelCommandGroup(
          new HandFloorLevel2(manuplatorSubsystem),
          new OnlyRamp(driveSubsystem)
        )
      );
    } else {
      if (taxi) {
        addCommands(
          new HandFloorLevel2(manuplatorSubsystem),
          new Taxi(driveSubsystem)
        );
      }
    }
  }
}
