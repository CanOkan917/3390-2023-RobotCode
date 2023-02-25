package com.team3390.robot.commands.manuplators;

import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ExtractCube extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;

  public ExtractCube(ManuplatorSubsystem manuplatorSubsystem) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {
    manuplatorSubsystem.cube_init();
  }

  @Override
  public void execute() {
    manuplatorSubsystem.cube_execute_extract();
  }

  @Override
  public void end(boolean interrupted) {
    manuplatorSubsystem.cube_end();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
