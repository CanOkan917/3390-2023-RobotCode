package frc.robot.utility;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class CompetitionShuffleboard {
  
  public ShuffleboardTab tab;

  public static CompetitionShuffleboard INSTANCE = new CompetitionShuffleboard();

  public CompetitionShuffleboard() {
    tab = Shuffleboard.getTab("Control Panel");
    Shuffleboard.selectTab("Control Panel");
  }

}
