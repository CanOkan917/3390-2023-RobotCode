package com.team3390.utility;

import com.team3390.Constants.LOWPOWERMODE_INCREASE_TYPE;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LowPowerMode extends SubsystemBase {
  
  public static LowPowerMode INSTANCE = new LowPowerMode();

  private final NetworkTableInstance networkTableInstance;
  private final NetworkTable networkTable;
  private final NetworkTableEntry driveMode;

  public LowPowerMode() {
    networkTableInstance = NetworkTableInstance.create();
    networkTable = networkTableInstance.getTable("lowpowermode");

    networkTable.setDefaultValue("driveMotorsEnabled", NetworkTableValue.makeBoolean(false));

    driveMode = networkTable.getEntry("driveMotorsEnabled");
  }

  public boolean getLowDriveModeEnabled() {
    return driveMode.getBoolean(false);
  }

  public void toggleLowDriveMode() {
    boolean current = driveMode.getBoolean(false);
    networkTable.putValue("driveMotorsEnabled", NetworkTableValue.makeBoolean(!current));
  }

  public CommandBase toggleLowDriveModeCommand() {
    return runOnce(() -> toggleLowDriveMode()).withName("toggleLowDriveMode");
  }

  public double calculate(double input, LOWPOWERMODE_INCREASE_TYPE type) {
    return (input / 4) * (type.ordinal() + 1);
  }

  public double calculate(int input, LOWPOWERMODE_INCREASE_TYPE type) {
    return (input / 4) * (type.ordinal() + 1);
  }

}
