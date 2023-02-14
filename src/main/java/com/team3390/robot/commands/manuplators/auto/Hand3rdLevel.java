package com.team3390.robot.commands.manuplators.auto;

import com.team3390.robot.Constants;
import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Hand3rdLevel extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;

  private final double speed;

  public Hand3rdLevel(ManuplatorSubsystem manuplatorSubsystem, double speed) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    this.speed = speed;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    manuplatorSubsystem.body(speed * -1);
    manuplatorSubsystem.elbow(speed * Constants.ELEVATOR_ELBOW_SPEED_COEFFICIENT);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return manuplatorSubsystem.isBodyOnTop();
  }

}
