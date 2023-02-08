package com.team3390.robot.commands.manuplators;

import java.util.function.Supplier;

import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class BodyUp extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;
  private final Supplier<Double> body, seconds;

  private double startTime;

  public BodyUp(ManuplatorSubsystem manuplatorSubsystem, Supplier<Double> body, Supplier<Double> seconds) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    this.body = body;
    this.seconds = seconds;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {
    manuplatorSubsystem.body(body.get());
  }

  @Override
  public void end(boolean interrupted) {
    manuplatorSubsystem.body(0);
  }

  @Override
  public boolean isFinished() {
    double time = Timer.getFPGATimestamp();
    return time - startTime > seconds.get();
  }
}
