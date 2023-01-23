package com.team3390.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.Supplier;

import com.team3390.robot.subsystems.DriveSubsystem;

public class TankDriveCommand extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;
  private final Supplier<Double> left, right;

  public TankDriveCommand(DriveSubsystem driveSubsystem, Supplier<Double> left, Supplier<Double> right) {
    this.driveSubsystem = driveSubsystem;
    this.left = left;
    this.right = right;
    addRequirements(driveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    driveSubsystem.tankDrivePercent(left.get(), right.get());
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
