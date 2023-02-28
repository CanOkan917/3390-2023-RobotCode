package com.team3390.robot.commands.manuplators.auto;

import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class HandFloorLevel extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;

  public HandFloorLevel(ManuplatorSubsystem manuplatorSubsystem) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    manuplatorSubsystem.body(1);
    manuplatorSubsystem.elbow(-0.1);
  }

  @Override
  public void end(boolean interrupted) {
    manuplatorSubsystem.body(0);
    manuplatorSubsystem.elbow(0);
    manuplatorSubsystem.bodyGyro.reset();
    manuplatorSubsystem.gyroOffset = 0;
  }

  @Override
  public boolean isFinished() {
    return manuplatorSubsystem.getAngle() <= 0;
  }

}
