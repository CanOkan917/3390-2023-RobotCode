package com.team3390.robot.commands.drive;

import java.util.function.Supplier;

import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.utility.PID;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class RotateToAngle extends CommandBase {

  private final DriveSubsystem driveSubsystem;
  private final Supplier<Integer> angle;

  private final PID pid = new PID(0.2, 0, 0.025, 3, 0.5, -0.5);

  public RotateToAngle(DriveSubsystem driveSubsystem, Supplier<Integer> angle) {
    this.driveSubsystem = driveSubsystem;
    this.angle = angle;
    addRequirements(driveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    double rot = pid.output(pid.calculate(driveSubsystem.getHeading(), angle.get()));
    driveSubsystem.arcadeDrivePercent(0, rot);
  }

  @Override
  public void end(boolean interrupted) {
    driveSubsystem.stopMotors();
  }

  @Override
  public boolean isFinished() {
    return pid.atSetpoint();
  }
}
