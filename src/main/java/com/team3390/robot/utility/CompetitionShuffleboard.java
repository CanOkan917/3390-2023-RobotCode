package com.team3390.robot.utility;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class CompetitionShuffleboard {
  
  public ShuffleboardTab tab;

  private static CompetitionShuffleboard instance;

  // DriveSubsystem
  public final GenericEntry robotBalancedEntry;
  public final GenericEntry robotLowPowerModeEntry;

  // LimelightSubsystem
  public final GenericEntry lmXAtSetpointEntry;
  public final GenericEntry lmYAtSetpointEntry;
  public final GenericEntry lmAtSetpointEntry;
  public final GenericEntry lmIsTargetEntry;
  public final GenericEntry lmVisionModeEntry;

  public static synchronized CompetitionShuffleboard getInstance() {
    if (instance == null) {
      instance = new CompetitionShuffleboard();
    }
    return instance;
  }

  public CompetitionShuffleboard() {
    tab = Shuffleboard.getTab("Control Panel");
    Shuffleboard.selectTab("Control Panel");

    robotBalancedEntry = tab.add("Balanced", false).getEntry();
    robotLowPowerModeEntry = tab.add("LP Enabled", false).getEntry();
    lmXAtSetpointEntry = tab.add("LM-X-AtSetpoint", false).getEntry();
    lmYAtSetpointEntry = tab.add("LM-Y-AtSetpoint", false).getEntry();
    lmAtSetpointEntry = tab.add("LM-AtSetpoint", false).getEntry();
    lmIsTargetEntry = tab.add("LM-IsTarget", false).getEntry();
    lmVisionModeEntry = tab.add("LM-VisionMode", "RETROREFLECTIVE").getEntry();
  }

}
