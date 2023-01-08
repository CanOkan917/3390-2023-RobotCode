package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class BalanceRobotCommand extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;

  public BalanceRobotCommand(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    addRequirements(this.driveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    driveSubsystem.balanceRobot();
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
