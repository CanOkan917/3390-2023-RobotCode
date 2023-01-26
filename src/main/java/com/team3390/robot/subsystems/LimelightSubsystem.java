package com.team3390.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.team3390.robot.utility.PID;
import com.team3390.robot.utility.TunableNumber;
import com.team3390.robot.Constants;
import com.team3390.robot.utility.CompetitionShuffleboard;

public class LimelightSubsystem extends SubsystemBase {

  private final NetworkTable networkTable;
  private final CompetitionShuffleboard shuffleBoard = CompetitionShuffleboard.getInstance();

  private final NetworkTableEntry tX; // Derece cinsinden dikey
  private final NetworkTableEntry tY; // Derece cinsinden yatay
  private final NetworkTableEntry tV; // Herhangi bir hedef var mı? (0, 1)
  private final NetworkTableEntry tA; // Hedefin kamerada ne kadar alan kapladığı
  private final NetworkTableEntry tL; // Limelight pipeline ping

  private final TunableNumber kP = new TunableNumber("Limelight/kP", Constants.LIMELIGHT_PID_KP);
  private final TunableNumber kI = new TunableNumber("Limelight/kI", Constants.LIMELIGHT_PID_KI);
  private final TunableNumber kD = new TunableNumber("Limelight/kD", Constants.LIMELIGHT_PID_KD);
  private final TunableNumber xTolerance = new TunableNumber("Limelight/xTolerance", Constants.LIMELIGHT_PID_X_TOLERANCE);
  private final TunableNumber yTolerance = new TunableNumber("Limelight/yTolerance", Constants.LIMELIGHT_PID_Y_TOLERANCE);

  private final PID xPID;
  private final PID yPID;

  private double lastTargetX;
  private double lastTargetY;

  private static LimelightSubsystem instance;

  public static synchronized LimelightSubsystem getInstance() {
    if (instance == null) {
      instance = new LimelightSubsystem();
    }
    return instance;
  }

  public LimelightSubsystem() {
    networkTable = NetworkTableInstance.getDefault().getTable("limelight");
    tX = networkTable.getEntry("tx");
    tY = networkTable.getEntry("ty");
    tV = networkTable.getEntry("tv");
    tA = networkTable.getEntry("ta");
    tL = networkTable.getEntry("tl");

    xPID = new PID(
      kP.get(), kI.get(), kD.get(),
      Constants.LIMELIGHT_PID_TOLERANCE,
      Constants.LIMELIGHT_PID_MAX_OUT,
      Constants.LIMELIGHT_PID_MIN_OUT
    );
    yPID = new PID(
      kP.get(), kI.get(), kD.get(),
      Constants.LIMELIGHT_PID_TOLERANCE,
      Constants.LIMELIGHT_PID_MAX_OUT,
      Constants.LIMELIGHT_PID_MIN_OUT
    );

    lastTargetX = 0;
    lastTargetY = 0;
  }

  @Override
  public void periodic() {
    if (!Constants.ROBOT_FIELD_MODE) {
      shuffleBoard.tab.add("LM-X-Output", getXOutput());
      shuffleBoard.tab.add("LM-X-Output", getYOutput());

      shuffleBoard.tab.add("LM-X", tX.getDouble(0));
      shuffleBoard.tab.add("LM-Y", tY.getDouble(0));

      shuffleBoard.tab.add("LM-Target-Area", tA.getDouble(0));
      shuffleBoard.tab.add("LM-Pipeline-Ping", tL.getDouble(0));
    }

    shuffleBoard.lmXAtSetpointEntry.setBoolean(XAtSetpoint());
    shuffleBoard.lmYAtSetpointEntry.setBoolean(YAtSetpoint());
    shuffleBoard.lmAtSetpointEntry.setBoolean(atSetpoint());
    shuffleBoard.lmIsTargetEntry.setBoolean(isTarget());
    shuffleBoard.lmVisionModeEntry.setString(getValue("pipeline").getInteger(0) == 0 ? "RETROREFLECTIVE" : getValue("pipeline").getInteger(0) == 1 ? "APRILTAGS" : "CUSTOM");
  }

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
   * Herhangi bir hedef olup olmadığını döndürür.
   * @return erğer hedef var ise true yok ise false
   */
  public boolean isTarget() {
    return tV.getDouble(0) == 1;
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

  /**
   * Eğer crosshair hedefin ortasına geldiyse true olarak döndürüyor.
   * Tolerance değerine göre değişir bu kısım
   * @return hedef noktada ise true, değilse false
   */
  public boolean atSetpoint() {
    return XAtSetpoint() && YAtSetpoint();
  }

  public boolean XAtSetpoint() {
    return Math.abs(tX.getDouble(0)) <= xTolerance.get();
  }

  public boolean YAtSetpoint() {
    return Math.abs(tY.getDouble(0)) <= yTolerance.get();
  }

  /**
   * Hedef noktaya ulaşılması için X ekseninde motorların ne kadar dönmesi gerektiğini döndürür.
   * @return yüzdelik güç
   */
  public double getXOutput() {
    if (this.isTarget()) {
      if (!xPID.atSetpoint()) {
        double x = tX.getDouble(0);
        this.lastTargetX = x;
        return xPID.output(xPID.calculate(x, 0));
      }
      return 0;
    }
    return xPID.output(xPID.calculate(this.lastTargetX, 0));
  }

  /**
   * Hedef noktaya ulaşılması için Y ekseninde motorların ne kadar dönmesi gerektiğini döndürür.
   * @return yüzdelik güç
   */
  public double getYOutput() {
    if (this.isTarget()) {
      if (!yPID.atSetpoint()) {
        double y = tY.getDouble(0);
        this.lastTargetY = y;
        return yPID.output(yPID.calculate(y, 0));
      }
      return 0;
    }
    return yPID.output(yPID.calculate(this.lastTargetY, 0));
  }

}
