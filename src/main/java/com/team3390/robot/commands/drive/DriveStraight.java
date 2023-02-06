package com.team3390.robot.commands.drive;

import com.team3390.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveStraight extends CommandBase {

  private final DriveSubsystem driveSubsystem;
  private final double speed;

  public DriveStraight(DriveSubsystem driveSubsystem, double speed) {
    this.driveSubsystem = driveSubsystem;
    this.speed = speed;
    addRequirements(this.driveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    driveSubsystem.driveStraight(speed, 0);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
