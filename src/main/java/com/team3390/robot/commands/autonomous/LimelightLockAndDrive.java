package com.team3390.robot.commands.autonomous;

import com.team3390.robot.commands.drive.BalanceRobotCommand;
import com.team3390.robot.commands.drive.DriveUntilCustomNavXRoll;
import com.team3390.robot.commands.drive.LockTargetCommand;
import com.team3390.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class LimelightLockAndDrive extends SequentialCommandGroup {
  public LimelightLockAndDrive(DriveSubsystem driveSubsystem) {
    addCommands(
      new LockTargetCommand(driveSubsystem),
      new WaitCommand(1),
      new DriveUntilCustomNavXRoll(driveSubsystem, () -> 10, () -> 0.6, () -> 0.0),
      new BalanceRobotCommand(driveSubsystem)
    );
  }
}
