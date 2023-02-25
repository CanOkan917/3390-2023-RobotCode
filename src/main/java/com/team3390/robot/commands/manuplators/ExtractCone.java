package com.team3390.robot.commands.manuplators;

import java.util.function.Supplier;

import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ExtractCone extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;

  public ExtractCone(ManuplatorSubsystem manuplatorSubsystem) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {
    manuplatorSubsystem.cone_init();
  }

  @Override
  public void execute() {
    manuplatorSubsystem.cone_execute_extract();
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
