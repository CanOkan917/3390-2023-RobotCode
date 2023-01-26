package com.team3390.robot.commands.autonomous;

import com.team3390.robot.Constants;
import com.team3390.robot.commands.drive.BalanceRobotCommand;
import com.team3390.robot.commands.drive.DriveUntilCustomNavXRoll;
import com.team3390.robot.commands.drive.LockRetroreflective;
import com.team3390.robot.commands.drive.RotateToAngle;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Pos5 extends SequentialCommandGroup {
  public Pos5(DriveSubsystem driveSubsystem, boolean balance, boolean always) {
    if (balance) {
      addCommands(
        new ResetSensorsCommand(driveSubsystem),
        new LockRetroreflective(driveSubsystem),
        new WaitCommand(0.5),
        new DriveUntilCustomNavXRoll(driveSubsystem, Constants.DRIVE_DETECT_ROLL, false, () -> 0.8, () -> 0.0),
        new BalanceRobotCommand(driveSubsystem, false),
        new RotateToAngle(driveSubsystem, () -> 0),
        new BalanceRobotCommand(driveSubsystem, always)
      );
    } else {
      addCommands(
        new ResetSensorsCommand(driveSubsystem),
        new LockRetroreflective(driveSubsystem),
        new WaitCommand(0.5)
      );
    }
  }
}
