package com.team3390.robot.subsystems;

import com.team3390.robot.Constants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimelightSubsystem extends SubsystemBase {

  private final NetworkTable networkTable;

  private static LimelightSubsystem instance;

  public static synchronized LimelightSubsystem getInstance() {
    if (instance == null) {
      instance = new LimelightSubsystem();
    }
    return instance;
  }

  public LimelightSubsystem() {
    networkTable = NetworkTableInstance.getDefault().getTable("limelight");
  }

  @Override
  public void periodic() {}

  /**
	 * Helper method to get an entry from the Limelight NetworkTable.
	 * 
	 * @param key Key for entry.
	 * @return NetworkTableEntry of given entry.
	 */
	public NetworkTableEntry getValue(String key) {
		return networkTable.getEntry(key);
	}

  /**
   * LED modunu ayarlar
   * @param mode LED modu
   */
  public void setLedMode(Constants.LIMELIGHT_LIGHT_MODE mode) {
    getValue("ledMode").setNumber(mode.ordinal());
  }

  /**
   * LED modunu ayarlar
   * @param mode LED modu
   */
  public CommandBase setLedModeCommand(Constants.LIMELIGHT_LIGHT_MODE mode) {
    return runOnce(() -> setLedMode(mode));
  }

  /**
   * Kamera modunu ayarlar
   * @param mode Kamera modu
   */
  public void setCamMode(Constants.LIMELIGHT_CAMERA_MODE mode) {
    getValue("camMode").setNumber(mode.ordinal());
  }

  /**
   * Kamera modunu ayarlar
   * @param mode Kamera modu
   */
  public CommandBase setCamModeCommand(Constants.LIMELIGHT_CAMERA_MODE mode) {
    return runOnce(() -> setCamMode(mode));
  }

  /**
   * Pipelineı değiştirir (0-9)
   * @param number Pipeline ID
   */
  public void setPipeline(int number) {
    getValue("pipeline").setNumber(number);
  }

  /**
   * Pipelineı değiştirir (0-9)
   * @param number Pipeline ID
   */
  public CommandBase setPipelineCommand(int number) {
    return runOnce(() -> getValue("pipeline").setNumber(number));
  }

}
