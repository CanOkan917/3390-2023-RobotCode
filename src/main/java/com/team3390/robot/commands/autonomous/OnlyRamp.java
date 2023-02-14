package com.team3390.robot.commands.autonomous;

import com.team3390.robot.Constants;
import com.team3390.robot.commands.drive.BalanceRobotCommand;
import com.team3390.robot.commands.drive.DriveUntilCustomNavXRoll;
import com.team3390.robot.commands.drive.RotateToAngle;
import com.team3390.robot.commands.utility.ResetSensorsCommand;
import com.team3390.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class OnlyRamp extends SequentialCommandGroup {
  public OnlyRamp(DriveSubsystem driveSubsystem, boolean always) {
    addCommands(
        new ResetSensorsCommand(driveSubsystem)
    // TODO Set arm for cone drop
    );
    addCommands(
        new DriveUntilCustomNavXRoll(driveSubsystem, Constants.DRIVE_DETECT_ROLL, false, () -> 0.8, () -> 0.0),
        new BalanceRobotCommand(driveSubsystem, false),
        new WaitCommand(0.5),
        new RotateToAngle(driveSubsystem, () -> 0),
        new BalanceRobotCommand(driveSubsystem, always)
    );
  }
}
