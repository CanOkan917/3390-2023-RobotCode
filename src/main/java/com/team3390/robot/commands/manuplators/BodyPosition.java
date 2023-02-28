package com.team3390.robot.commands.manuplators;

import java.util.function.Supplier;

import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class BodyPosition extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;
  private final Supplier<Double> angle;

  public BodyPosition(ManuplatorSubsystem manuplatorSubsystem, Supplier<Double> angle) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    this.angle = angle;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    manuplatorSubsystem.body(angle.get() < manuplatorSubsystem.bodyGyro.getAngle() ? -1 : 1);
  }

  @Override
  public void end(boolean interrupted) {
    manuplatorSubsystem.body(0);
    manuplatorSubsystem.bodyGyro.reset();
    manuplatorSubsystem.gyroOffset = angle.get();
  }

  @Override
  public boolean isFinished() {
    if (Math.abs(manuplatorSubsystem.getAngle()) > angle.get()) 
      return Math.abs(manuplatorSubsystem.getAngle()) < angle.get();
    else
      return Math.abs(manuplatorSubsystem.getAngle()) >= angle.get();
  }
}
