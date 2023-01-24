package com.team3390.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;

import com.team3390.robot.Constants;
import com.team3390.robot.subsystems.DriveSubsystem;

public class BalanceRobotCommand extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;

  public BalanceRobotCommand(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
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
    return Math.abs(driveSubsystem.getRobotRoll()) < Constants.DRIVE_NAVX_ROLL_DEADBAND;
  }
}
