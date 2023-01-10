package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C.Port;

public final class Constants {

  /**
   * Yarışma modu ShuffleBoard'ı otomatık olarak yapılandırır.
   */
  public static final boolean ROBOT_FIELD_MODE = DriverStation.isFMSAttached();

  /**
   * Joystick portlarınının tanımlanması
   */
  public static final int JOYSTICK_LEFT_PORT = 0;
  public static final int JOYSTICK_RIGHT_PORT = 1;
  public static final int JOYSTICK_ATARI1_PORT = 3;
  public static final int JOYSTICK_ATARI2_PORT = 2;

  /**
   * Sürüş motorları ID'lerinin tanımlanması
   */
  public static final int DRIVE_LEFT_LEADER_ID = 0;
  public static final int DRIVE_LEFT_MOTOR2_ID = 1;
  public static final int DRIVE_RIGHT_LEADER_ID = 2;
  public static final int DRIVE_RIGHT_MOTOR2_ID = 3;
  public static final boolean DRIVE_LEFT_INVERTED = false;
  public static final boolean DRIVE_RIGHT_INVERTED = false;
  public static final double DRIVE_BALANCE_PID_KP = 0.4;
  public static final double DRIVE_BALANCE_PID_KI = 0;
  public static final double DRIVE_BALANCE_PID_KD = 0;
  public static final double DRIVE_BALANCE_PID_TOLERANCE = 0;
  public static final double DRIVE_BALANCE_PID_MAXOUT = 0.5;
  public static final double DRIVE_BALANCE_PID_MINOUT = 0.5 * -1;

  /**
   * Sensörler
   */
  public static final Port SENSOR_NAVX_PORT = Port.kMXP;

  /**
   * LowPowerMode
   */
  public static enum LOWPOWERMODE_INCREASE_TYPE {
    ONE, TWO, TREE
  }

  /**
   * Limelight değerleri
   */
  public static final double LIMELIGHT_PID_KP = 0.4;
  public static final double LIMELIGHT_PID_KI = 0;
  public static final double LIMELIGHT_PID_KD = 0;
  public static final double LIMELIGHT_PID_TOLERANCE = 0;
  public static final double LIMELIGHT_PID_MAX_OUT = 0.7;
  public static final double LIMELIGHT_PID_MIN_OUT = -0.7;
  public static enum LIMELIGHT_LIGHT_MODE {
    PIPELINE_VALUE, OFF, BLINK, ON
  }
  public static enum LIMELIGHT_CAMERA_MODE {
    VISION, DRIVE
  }
  public static enum LIMELIGHT_LOCK_MODE {
    X, Y, ALL
  }

}
