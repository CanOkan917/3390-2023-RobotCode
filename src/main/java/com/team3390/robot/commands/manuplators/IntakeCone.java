package com.team3390.robot.commands.manuplators;

import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class IntakeCone extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;

  public IntakeCone(ManuplatorSubsystem manuplatorSubsystem) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {
    manuplatorSubsystem.cone_init();
  }

  @Override
  public void execute() {
    manuplatorSubsystem.cone_execute_intake();
  }

  @Override
  public void end(boolean interrupted) {
    manuplatorSubsystem.cone_end();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
