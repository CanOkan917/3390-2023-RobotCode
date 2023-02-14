package com.team3390.robot.commands.autonomous;

import com.team3390.robot.Constants;
import com.team3390.robot.commands.drive.DriveStraight;
import com.team3390.robot.commands.drive.LockAprilTags;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.LimelightSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Cube extends SequentialCommandGroup {
  private final LimelightSubsystem limelightSubsystem = LimelightSubsystem.getInstance();

  public Cube(DriveSubsystem driveSubsystem, boolean balance, boolean always) {
    Command aimForCube;

    limelightSubsystem.setLedMode(Constants.LIMELIGHT_LIGHT_MODE.PIPELINE_VALUE);
    limelightSubsystem.setPipeline(1);

    if (limelightSubsystem.isTarget()) {
      aimForCube = new LockAprilTags(driveSubsystem);
    } else {
      aimForCube = new ParallelDeadlineGroup(
        new WaitCommand(1),
        new DriveStraight(driveSubsystem, 0.8)
      );
    }
    
    addCommands(
      new ResetSensorsCommand(driveSubsystem),
      aimForCube
      // TODO Set arm for cube drop
    );

    if (balance) {
      addCommands(
        new OnlyRamp(driveSubsystem, always)
      );
    }
   }
}
