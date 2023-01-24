package com.team3390.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;

import com.team3390.robot.Constants.LIMELIGHT_CAMERA_MODE;
import com.team3390.robot.Constants.LIMELIGHT_LIGHT_MODE;
import com.team3390.robot.subsystems.DriveSubsystem;
import com.team3390.robot.subsystems.LimelightSubsystem;

public class LockTargetCommand extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;
  private final LimelightSubsystem limelight = LimelightSubsystem.getInstance();

  public LockTargetCommand(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    addRequirements(driveSubsystem);
  }

  @Override
  public void initialize() {
    limelight.setCamMode(LIMELIGHT_CAMERA_MODE.VISION);
    limelight.setLedMode(LIMELIGHT_LIGHT_MODE.ON);
  }

  @Override
  public void execute() {
    driveSubsystem.lockTarget();
  }

  @Override
  public void end(boolean interrupted) {
    limelight.setLedMode(LIMELIGHT_LIGHT_MODE.PIPELINE_VALUE);
  }

  @Override
  public boolean isFinished() {
    return limelight.atSetpoint();
  }
}
