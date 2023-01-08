package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class TankDriveCommand extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;
  private final Joystick left;
  private final Joystick right;

  public TankDriveCommand(DriveSubsystem driveSubsystem, Joystick left, Joystick right) {
    this.driveSubsystem = driveSubsystem;
    this.left = left;
    this.right = right;
    addRequirements(this.driveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    driveSubsystem.tankDrivePercent(left.getY(), right.getY());
  }

  @Override
  public void end(boolean interrupted) {
    driveSubsystem.stopMotors();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
