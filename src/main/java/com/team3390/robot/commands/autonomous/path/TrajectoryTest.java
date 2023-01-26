package com.team3390.robot.commands.autonomous.path;

import com.team3390.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class TrajectoryTest extends SequentialCommandGroup {
  public TrajectoryTest(DriveSubsystem driveSubsystem) {
    // https://github.com/mjansen4857/pathplanner/wiki/PathPlannerLib:-Java-Usage
    addCommands();
  }
}
