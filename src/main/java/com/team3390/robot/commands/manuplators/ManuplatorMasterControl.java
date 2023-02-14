package com.team3390.robot.commands.manuplators;

import java.util.function.Supplier;

import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManuplatorMasterControl extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;
  private final Supplier<Double> body, elbow;

  public ManuplatorMasterControl(ManuplatorSubsystem manuplatorSubsystem, Supplier<Double> body, Supplier<Double> elbow) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    this.body = body;
    this.elbow = elbow;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    manuplatorSubsystem.body(body.get());

    // if (Math.abs(elbow.get()) < 0.15 && Math.abs(body.get()) > 0.05)
    //   manuplatorSubsystem.elbow(Math.copySign(Constants.ELEVATOR_ELBOW_SPEED_COEFFICIENT, body.get()));
    // else
    //   manuplatorSubsystem.elbow(elbow.get());
    manuplatorSubsystem.elbow(elbow.get());
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
