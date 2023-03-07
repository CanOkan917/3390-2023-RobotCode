package com.team3390.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;

import com.team3390.robot.subsystems.DriveSubsystem;

public class BalanceRobotCommandWithPID extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;
  private final boolean always;

  public BalanceRobotCommandWithPID(DriveSubsystem driveSubsystem, boolean always) {
    this.driveSubsystem = driveSubsystem;
    this.always = always;
    addRequirements(driveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    driveSubsystem.balanceRobot();
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    // return Math.abs(driveSubsystem.getRobotRoll()) < Constants.DRIVE_NAVX_ROLL_DEADBAND;
    if (always) {
      return false;
    }
    return driveSubsystem.isBalanced();
  }
}
