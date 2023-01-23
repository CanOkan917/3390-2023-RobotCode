package com.team3390.robot.utility;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class CompetitionShuffleboard {
  
  public ShuffleboardTab tab;

  public static CompetitionShuffleboard INSTANCE = new CompetitionShuffleboard();

  public final GenericEntry robotBalancedEntry;
  public final GenericEntry robotLowPowerModeEntry;

  public CompetitionShuffleboard() {
    tab = Shuffleboard.getTab("Control Panel");
    Shuffleboard.selectTab("Control Panel");

    robotBalancedEntry = tab.add("Balanced", false).getEntry();
    robotLowPowerModeEntry = tab.add("LowPowerMode-DriveMotors Enabled", false).getEntry();  }

}
