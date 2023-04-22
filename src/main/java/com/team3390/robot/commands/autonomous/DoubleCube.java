package com.team3390.robot.commands.autonomous;

import com.team3390.robot.commands.drive.DriveStraight;
import com.team3390.robot.commands.drive.RotateToAngle;
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

public class DoubleCube extends SequentialCommandGroup {

  public DoubleCube(DriveSubsystem driveSubsystem, ManuplatorSubsystem manuplatorSubsystem) {
    manuplatorSubsystem.bodyGyro.reset();

    addCommands(
      new ResetSensorsCommand(driveSubsystem),
      new Hand3rdLevel(manuplatorSubsystem),
      new ParallelDeadlineGroup(
        new WaitCommand(0.5),
        new DriveStraight(driveSubsystem, -0.6)  
      ),
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
      ),
      new RotateToAngle(driveSubsystem, () -> 180),
      new ParallelCommandGroup(
        new ParallelDeadlineGroup(
          new WaitCommand(3),
          new DriveStraight(driveSubsystem, -0.5)
        ),
        new IntakeCube(manuplatorSubsystem)
      )
    );
  }
}
