/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.util.Color;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final I2C.Port i2cPort = I2C.Port.kOnboard; // Initializes the I2C port on the RoboRIO for the Color Sensor - D
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort); // Initializes the color sensor for use
  private final ColorMatch m_colorMatcher = new ColorMatch(); // Creates an object that lets us match colours

  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429); // This line establishes what should be a like-blue colour
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240); // This line establishes what should be a like-green colour
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114); // This line establishes what should be a like-red colour
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113); // This line establishes what should be a like-yellow colour
  private RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.

    m_colorMatcher.addColorMatch(kBlueTarget); // These lines create the objects that let us match the colours to what they should be
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);

    m_robotContainer = new RobotContainer();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();

    Color detectedColor = m_colorSensor.getColor(); // Gets the colour reading from the colour sensor

    String colorString; // Creates our string that we can use for the Smart Dashboard
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor); // Matches what we got from the colour sensor to the closest colour we have listed

    if (match.color == kBlueTarget) {
      colorString = "Blue"; // If the colour we got is closer to the blue target, set the string to Blue.
    } else if (match.color == kRedTarget) {
      colorString = "Red"; // If the colour we got is closer to the red target, set the string to Red.
    } else if (match.color == kGreenTarget) {
      colorString = "Green"; // If the colour we got is closer to the green target, set the string to Green.
    } else if (match.color == kYellowTarget) {
      colorString = "Yellow"; // If the colour we got is closer to the yellow target, set the string to Yellow.
    } else {
      colorString = "Unknown"; // If a colour doesn't match any of the above, the colour is unknown and we should disregard it.
    }
    /*
    * The below section sends the data we recieved to the Smart Dashboard
    */
    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("Confidence", match.confidence);
    SmartDashboard.putString("Detected Color", colorString);

  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
