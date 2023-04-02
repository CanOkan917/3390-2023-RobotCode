package com.team3390.robot.commands.drive;

import com.team3390.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

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
    driveSubsystem.driveStraight(0.65, 0);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return driveSubsystem.getRobotRoll() > -3 || driveSubsystem.getRobotRoll() < 3;
  }
}
