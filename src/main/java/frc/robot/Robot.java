package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;


import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;


public class Robot extends TimedRobot {
  private final Joystick driverJoystick = new Joystick(0);
  private final GenericHID opperatorJoystick = new GenericHID(1);

  PWMSparkMax r1 = new PWMSparkMax(9);
  PWMSparkMax r2 = new PWMSparkMax(8);
  PWMSparkMax r3 = new PWMSparkMax(7);
  MotorControllerGroup rightSide = new MotorControllerGroup(r1, r2, r3);

  PWMSparkMax l1 = new PWMSparkMax(0);
  PWMSparkMax l2 = new PWMSparkMax(1);
  PWMSparkMax l3 = new PWMSparkMax(2);
  MotorControllerGroup leftSide = new MotorControllerGroup(l1, l2, l3);

  TalonSRX armMotor = new TalonSRX(1);
  TalonSRX gripperMotor = new TalonSRX(0);

  // Encoder encoder = new Encoder(5, 7, false, CounterBase.EncodingType.k4X);
  SendableChooser<Command> m_Chooser = new SendableChooser<>();
  // CANCoder _coder = new CANCoder(1);
  // int _loopCount = 0;

  private final Timer clock = new Timer();
  private final DifferentialDrive drive = new DifferentialDrive(leftSide,rightSide);

  AnalogGyro gyro = new AnalogGyro(0);

  DigitalInput funnyClickyThing = new DigitalInput(0);

  @Override
  public void robotInit() {
    armMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

    leftSide.setInverted(true);
    rightSide.setInverted(false);    
    
    UsbCamera camera = CameraServer.startAutomaticCapture();
    camera.setResolution(4000, 4000);

    Shuffleboard.getTab("Gyro Tab").add(gyro);
    
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    drive.stopMotor();
  }

  @Override
  public void autonomousInit() {
    clock.restart();
  }

  @Override
  public void autonomousPeriodic() {
    // if (clock.get() < 2.0) {
    //   m_robotDrive.arcadeDrive(0.8, 0.0, false);
    // } else if (clock.get() < 3.5){
    //   m_robotDrive.arcadeDrive(0.5,0.81, false); // stop robot
    // }


    if (clock.get() < 4) {
      drive.arcadeDrive(0.5, 0.0, false);
    } else if (clock.get() < 6.5) { 
      drive.arcadeDrive(-0.4, 0.0, false);
    } else {
      drive.stopMotor();
    }

    // cool auto arm yay!!!
    // if (clock.get() < 2) {
    //   l4.set(ControlMode.PercentOutput, 0.1);
    // } else {
    //   l4.set(ControlMode.PercentOutput, 0);
    // }
  }

  @Override
  public void teleopInit() {}


  @Override
  public void teleopPeriodic() {

    drive.arcadeDrive(-driverJoystick.getY(), -driverJoystick.getX());

    var gripper = opperatorJoystick.getRawAxis(1);
    if (gripper > 0) {
      gripperMotor.set(ControlMode.PercentOutput, -gripper/1.1);
    }
    if (gripper == 0) {
      gripperMotor.set(ControlMode.PercentOutput, 0);
    }

    var gripper2 = opperatorJoystick.getRawAxis(1);
    if (gripper2 < 0) {
      gripperMotor.set(ControlMode.PercentOutput, -gripper2/1.1);
    } 
    
    if (gripper2 == 0){
      gripperMotor.set(ControlMode.PercentOutput, 0);
    }


    
    var motion = opperatorJoystick.getRawAxis(5);
    
    if (motion < 0 && motion > -0.7) {
      armMotor.set(ControlMode.PercentOutput, -motion/3);
    }
    if (motion == 0) {
      armMotor.set(ControlMode.PercentOutput, 0);
    }

    var motion2 = opperatorJoystick.getRawAxis(5);

    if (motion2 > 0 && motion < 0.7) {
      armMotor.set(ControlMode.PercentOutput, -motion2/3);
    }
    if (motion2 == 0) {
      armMotor.set(ControlMode.PercentOutput, 0);
    }

  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }
}
