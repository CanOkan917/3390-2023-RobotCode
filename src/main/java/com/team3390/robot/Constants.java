package com.team3390.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C.Port;

public final class Constants {
  public static final boolean ROBOT_FIELD_MODE = DriverStation.isFMSAttached();
  public static final boolean TUNING_MODE = false;

  public static final int JOYSTICK_LEFT_PORT = 0;
  public static final int JOYSTICK_RIGHT_PORT = 1;
  public static final int JOYSTICK_GAMEPAD_PORT = 4;

  public static final int     DRIVE_LEFT_MASTER_ID = 11;
  public static final int     DRIVE_LEFT_SLAVE1_ID = 10;
  public static final int     DRIVE_LEFT_SLAVE2_ID = 9;
  public static final int     DRIVE_RIGHT_MASTER_ID = 0;
  public static final int     DRIVE_RIGHT_SLAVE1_ID = 1;
  public static final int     DRIVE_RIGHT_SLAVE2_ID = 2;
  public static final boolean DRIVE_LEFT_INVERTED = true;
  public static final boolean DRIVE_RIGHT_INVERTED = false;
  public static final int     DRIVE_NAVX_ROLL_DEADBAND = 2;
  public static final double  DRIVE_BALANCE_PID_KP = 0.21;
  public static final double  DRIVE_BALANCE_PID_KI = 0.05;
  public static final double  DRIVE_BALANCE_PID_KD = 0.01;
  public static final double  DRIVE_BALANCE_PID_TOLERANCE = 3;
  public static final double  DRIVE_BALANCE_PID_MAXOUT = 0.5;
  public static final double  DRIVE_BALANCE_PID_MINOUT = DRIVE_BALANCE_PID_MAXOUT * -1;
  public static final int     DRIVE_DETECT_ROLL = 10;

  public static final int     ELEVATOR_BODY_ID = 3;
  public static final boolean ELEVATOR_BODY_INVERTED = true;
  public static final int     ELEVATOR_ELBOW_ID = 4;
  public static final boolean ELEVATOR_ELBOW_INVERTED = true;
  public static final double  ELEVATOR_ELBOW_SPEED_COEFFICIENT = 0.185;
  public static final int     ELEVATOR_HAND_ID = 5;
  public static final boolean ELEVATOR_HAND_INVERTED = false;
  public static final int     ELEVATOR_INTAKE_SOLENOID_PORT = 3;
  public static final int     ELEVATOR_BODY_MAX_ANGLE = 120;

  public static final Port SENSOR_NAVX_PORT = Port.kMXP;
  public static final int  SENSOR_PRESSURE_PORT = 0;
  public static final int  SENSOR_BODY_GYRO_PORT = 1;

  public static enum LOWPOWERMODE_INCREASE_TYPE {
    ONE, TWO, TREE
  }

  public static final double LIMELIGHT_PID_KP = 0.19;
  public static final double LIMELIGHT_PID_KI = 0.01;
  public static final double LIMELIGHT_PID_KD = 0.001;
  public static final double LIMELIGHT_PID_TOLERANCE = 0;
  public static final double LIMELIGHT_PID_MAX_OUT = 0.7;
  public static final double LIMELIGHT_PID_MIN_OUT = LIMELIGHT_PID_MAX_OUT * -1;
  public static final double LIMELIGHT_PID_X_RETRO_TOLERANCE = 0.75;
  public static final double LIMELIGHT_PID_Y_RETRO_TOLERANCE = 1.5;
  public static final double LIMELIGHT_PID_X_APRIL_TOLERANCE = 1;
  public static final double LIMELIGHT_PID_Y_APRIL_TOLERANCE = 0.2;
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
