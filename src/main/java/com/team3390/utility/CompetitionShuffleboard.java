package com.team3390.utility;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class CompetitionShuffleboard {
  
  public ShuffleboardTab tab;

  public static CompetitionShuffleboard INSTANCE = new CompetitionShuffleboard();

  public final GenericEntry robotBalancedEntry = tab.add("Balanced", false).getEntry();
  public final GenericEntry robotLowPowerModeEntry = tab.add("LowPowerMode-DriveMotors Enabled", false).getEntry();
  public final GenericEntry robotPitch = tab.add("Roll", 0).getEntry();
  public final GenericEntry robotBalancePIDOutput = tab.add("BalancePID-OUTPUT", 0).getEntry();

  public CompetitionShuffleboard() {
    tab = Shuffleboard.getTab("Control Panel");
    Shuffleboard.selectTab("Control Panel");
  }

}
