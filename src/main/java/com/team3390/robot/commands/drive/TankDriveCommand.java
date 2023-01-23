package com.team3390.robot.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;

import com.team3390.robot.subsystems.DriveSubsystem;

public class TankDriveCommand extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;
  private final Joystick left, right;

  public TankDriveCommand(DriveSubsystem driveSubsystem, Joystick left, Joystick right) {
    this.driveSubsystem = driveSubsystem;
    this.left = left;
    this.right = right;
    addRequirements(driveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    driveSubsystem.tankDrivePercent(left.getY(), right.getY());
  }

  @Override
  public void end(boolean interrupted) {
    driveSubsystem.stopMotors();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
