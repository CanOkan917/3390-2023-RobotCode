package com.team3390.robot.commands.manuplators;

import java.util.function.Supplier;

import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManuplatorMasterControl extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;
  private final Supplier<Double> body, hand;

  public ManuplatorMasterControl(ManuplatorSubsystem manuplatorSubsystem, Supplier<Double> body, Supplier<Double> hand) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    this.body = body;
    this.hand = hand;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    manuplatorSubsystem.hand(hand.get());
    manuplatorSubsystem.body(body.get());
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }
}
