package com.team3390.robot.commands.manuplators;

import java.util.function.Supplier;

import com.team3390.robot.subsystems.ManuplatorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class IntakeCube extends CommandBase {

  private final ManuplatorSubsystem manuplatorSubsystem;

  public IntakeCube(ManuplatorSubsystem manuplatorSubsystem) {
    this.manuplatorSubsystem = manuplatorSubsystem;
    addRequirements(manuplatorSubsystem);
  }

  @Override
  public void initialize() {
    manuplatorSubsystem.cube_init();
  }

  @Override
  public void execute() {
    manuplatorSubsystem.cube_execute_intake();
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
