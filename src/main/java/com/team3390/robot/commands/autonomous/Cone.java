package com.team3390.robot.commands.autonomous;

import com.team3390.robot.Constants;
import com.team3390.robot.commands.drive.BalanceRobotCommand;
import com.team3390.robot.commands.drive.DriveStraight;
import com.team3390.robot.commands.drive.DriveUntilCustomNavXRoll;
import com.team3390.robot.commands.drive.LockRetroreflective;
import com.team3390.robot.commands.drive.RotateToAngle;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.LimelightSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Cone extends SequentialCommandGroup {
  private final LimelightSubsystem limelightSubsystem = LimelightSubsystem.getInstance();

  public Cone(DriveSubsystem driveSubsystem, boolean balance, boolean always) {
    Command aimForCone;

    limelightSubsystem.setLedMode(Constants.LIMELIGHT_LIGHT_MODE.PIPELINE_VALUE);
    limelightSubsystem.setPipeline(1);

    if (limelightSubsystem.isTarget()) {
      aimForCone = new LockRetroreflective(driveSubsystem);
    } else {
      aimForCone = new ParallelDeadlineGroup(
        new WaitCommand(1),
        new DriveStraight(driveSubsystem, 0.8)
      );
    }

    addCommands(
      new ResetSensorsCommand(driveSubsystem),
      aimForCone
      // TODO Set arm for cone drop
    );

    if (balance) {
      addCommands(
        new DriveUntilCustomNavXRoll(driveSubsystem, Constants.DRIVE_DETECT_ROLL, false, () -> 0.9, () -> 0.0),
        new BalanceRobotCommand(driveSubsystem, false),
        new RotateToAngle(driveSubsystem, () -> 0),
        new BalanceRobotCommand(driveSubsystem, always)
      );
    }
  }
}
