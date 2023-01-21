package frc.robot.commands.utility;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class ResetSensorsCommand extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;

  public ResetSensorsCommand(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    addRequirements(this.driveSubsystem);
  }

  @Override
  public void initialize() {
    driveSubsystem.resetSensors();
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
