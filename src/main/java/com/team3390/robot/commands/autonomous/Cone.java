package com.team3390.robot.commands.autonomous;

import com.team3390.robot.commands.drive.DriveStraight;
import com.team3390.robot.commands.manuplators.ExtractCone;
import com.team3390.robot.commands.manuplators.IntakeCone;
import com.team3390.robot.commands.manuplators.auto.Hand3rdLevel;
import com.team3390.robot.commands.manuplators.auto.HandFloorLevel;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Cone extends SequentialCommandGroup {
  public Cone(DriveSubsystem driveSubsystem, ManuplatorSubsystem manuplatorSubsystem, boolean balance, boolean always) {
    addCommands(
      new ResetSensorsCommand(driveSubsystem),
      new ParallelDeadlineGroup(
        new WaitCommand(0.1),
        new IntakeCone(manuplatorSubsystem)
      ),
      new Hand3rdLevel(manuplatorSubsystem),
      new WaitCommand(1),
      new ParallelDeadlineGroup(
        new WaitCommand(0.1),
        new ExtractCone(manuplatorSubsystem)
      ),
      new ParallelDeadlineGroup(
        new WaitCommand(1),
        new DriveStraight(driveSubsystem, 0.7)  
      )
    );

    if (balance) {
      addCommands(
        new ParallelCommandGroup(
          new HandFloorLevel(manuplatorSubsystem),
          new OnlyRamp(driveSubsystem, always)
        )
      );
    } else {
      addCommands(
        new HandFloorLevel(manuplatorSubsystem)
      );
    }
   }
}
