package com.team3390.robot.utility;

import com.team3390.robot.Constants;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class CompetitionShuffleboard {
  
  public ShuffleboardTab tab;

  private static CompetitionShuffleboard instance;

  // DriveSubsystem
  public final GenericEntry robotBalancedEntry;
  public final GenericEntry robotLowPowerModeEntry;

  // Pneumatics
  public final GenericEntry robotTankPressureEntry;

  public static synchronized CompetitionShuffleboard getInstance() {
    if (instance == null) {
      instance = new CompetitionShuffleboard();
    }
    return instance;
  }

  public CompetitionShuffleboard() {
    tab = Shuffleboard.getTab("Control Panel");
    if (Constants.ROBOT_FIELD_MODE) {
      Shuffleboard.selectTab("Control Panel");
    }

    robotBalancedEntry = tab.add("Balanced", false).getEntry();
    robotLowPowerModeEntry = tab.add("LP Enabled", false).getEntry();
    robotTankPressureEntry = tab.add("Tank Pressure", 0.0).getEntry();
  }

}
