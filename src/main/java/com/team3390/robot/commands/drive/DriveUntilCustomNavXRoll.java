package com.team3390.robot.commands.drive;

import java.util.function.Supplier;

import com.team3390.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveUntilCustomNavXRoll extends CommandBase {

  private final DriveSubsystem driveSubsystem;
  private final Supplier<Integer> targetRoll;
  private final Supplier<Double> fwd;
  private final Supplier<Double> rot;

  public DriveUntilCustomNavXRoll(DriveSubsystem driveSubsystem, Supplier<Integer> targetRoll, Supplier<Double> fwd, Supplier<Double> rot) {
    this.driveSubsystem = driveSubsystem;
    this.targetRoll = targetRoll;
    this.fwd = fwd;
    this.rot = rot;
    addRequirements(this.driveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    driveSubsystem.driveStraight(fwd.get(), rot.get());
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return driveSubsystem.getRobotRoll() >= targetRoll.get();
  }
}
