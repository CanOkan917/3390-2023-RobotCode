package com.team3390.robot.commands.drive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import com.team3390.robot.subsystems.DriveSubsystem;

public class BalanceRobotCommand extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;

  private int i = 0;

  public BalanceRobotCommand(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    addRequirements(driveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    if (i != Math.round(Timer.getFPGATimestamp())) {
      driveSubsystem.driveStraight(0.3, 0);
      i = (int) Math.round(Timer.getFPGATimestamp());
    } else {
      driveSubsystem.stopMotors();
    }
    SmartDashboard.putNumber("i", (int) Math.round(Timer.getFPGATimestamp()));
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return driveSubsystem.getRobotRoll() > -3 || driveSubsystem.getRobotRoll() < 3;
  }
}
