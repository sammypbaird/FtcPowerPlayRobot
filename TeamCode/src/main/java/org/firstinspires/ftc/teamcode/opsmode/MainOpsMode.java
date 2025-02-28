/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.opsmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an example of a Linear "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode is executed.
 *
 * This particular OpMode illustrates driving a 4-motor Omni-Directional (or Holonomic) robot.
 * This code will work with either a Mecanum-Drive or an X-Drive train.
 * Both of these drives are illustrated at https://gm0.org/en/latest/docs/robot-design/drivetrains/holonomic.html
 * Note that a Mecanum drive must display an X roller-pattern when viewed from above.
 *
 * Also note that it is critical to set the correct rotation direction for each motor.  See details below.
 *
 * Holonomic drives provide the ability for the robot to move in three axes (directions) simultaneously.
 * Each motion axis is controlled by one Joystick axis.
 *
 * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
 * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
 * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
 *
 * This code is written assuming that the right-side motors need to be reversed for the robot to drive forward.
 * When you first test your robot, if it moves backward when you push the left stick forward, then you must flip
 * the direction of all 4 motors (see code below).
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Main OpMode")
public class MainOpsMode extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor liftDrive = null;
    private Servo claw = null;
    private Servo wrist = null;

    private static final double SPEED = 1.0;

    //All the numbers below should be found through calibration
    private static final int ENCODER_TIMEOUT = 5;
    private static final int JUNCTION_ENCODING_SHORT = 2800;
    private static final int JUNCTION_ENCODING_MEDIUM = 4230;
    private static final int JUNCTION_ENCODING_TALL = 6300;

    private static final double CLAW_POSITION_OPEN = 0.4;
    private static final double CLAW_POSITION_CLOSED = 0.7;
    private static final double WRIST_POSITION_LEVEL = 0.55;
    private static final double WRIST_POSITION_DOWN = 0.2;
    private static final double LIFT_MOTOR_SPEED = 1.0;
    private static final double DRIVE_SPEED = 0.6;
    private int liftPosition = 0;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        liftDrive = hardwareMap.get(DcMotor.class, "lift_drive");
        claw = hardwareMap.get(Servo.class, "claw");
        wrist = hardwareMap.get(Servo.class, "wrist");

        //set directions
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        //reset the encoder
        liftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftDrive.setTargetPosition(0);
        liftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftDrive.setPower(SPEED);

        wrist.setPosition(WRIST_POSITION_LEVEL);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();
        claw.setPosition(CLAW_POSITION_CLOSED);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            updateDriveMotors();
            updateLift();
            updateClaw();

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }

    private void updateDriveMotors() {
        double max;

        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
        double lateral = gamepad1.left_stick_x;
        double yaw = gamepad1.right_stick_x;

        //fine control using dpad and bumpers
        if (gamepad1.dpad_up)
            axial += 0.3;
        if (gamepad1.dpad_down)
            axial -= 0.3;
        if (gamepad1.dpad_left)
            lateral -= 0.3;
        if (gamepad1.dpad_right)
            lateral += 0.3;
        if (gamepad1.left_bumper)
            yaw -= 0.3;
        if (gamepad1.right_bumper)
            yaw += 0.3;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double leftFrontPower = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower = axial - lateral + yaw;
        double rightBackPower = axial + lateral - yaw;

        leftFrontPower *= DRIVE_SPEED;
        rightFrontPower *= DRIVE_SPEED;
        leftBackPower *= DRIVE_SPEED;
        rightBackPower *= DRIVE_SPEED;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // Send calculated power to wheels
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);

        telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
    }

    private void updateLift() {
        //manual control
        if (Math.abs(gamepad2.left_stick_y) > 0.15) {
            // if (liftDrive.getCurrentPosition() <= JUNCTION_ENCODING_TALL && liftDrive.getCurrentPosition() >= 0) {
            liftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftDrive.setPower(gamepad2.left_stick_y);
            // }
        }
        //while right trigger is pressed, do manual control
        else if (gamepad2.right_trigger > 0.5) {
            if (gamepad2.a)
                liftDrive.setTargetPosition(0);
            else if (gamepad2.b)
                liftDrive.setTargetPosition(-JUNCTION_ENCODING_SHORT);
            else if (gamepad2.x)
                liftDrive.setTargetPosition(-JUNCTION_ENCODING_MEDIUM);
            else if (gamepad2.y)
                liftDrive.setTargetPosition(-JUNCTION_ENCODING_TALL);
            liftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftDrive.setPower(LIFT_MOTOR_SPEED);
        } else {
            liftDrive.setTargetPosition(liftDrive.getCurrentPosition());
            liftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        telemetry.addData("Lift",  " at %7d", liftDrive.getCurrentPosition());
    }

    private void updateClaw() {
        // First callibrate
        if (gamepad2.left_bumper) {
            claw.setPosition(CLAW_POSITION_OPEN);
            telemetry.addData("Button", "Left bumper");
            telemetry.update();
        }
        else if (gamepad2.right_bumper)
            claw.setPosition(CLAW_POSITION_CLOSED);

        if (gamepad2.a && gamepad2.right_trigger < 0.5) {
            wrist.setPosition(WRIST_POSITION_LEVEL);
        } else if (gamepad2.b && gamepad2.right_trigger < 0.5) {
            wrist.setPosition(WRIST_POSITION_DOWN);
        }
    }

    /*
    public void encoderDrive(double speed, int encoderTarget, double timeoutS) {

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            liftDrive.setTargetPosition(encoderTarget);

            // Turn On RUN_TO_POSITION
            liftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            liftDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (liftDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d", encoderTarget);
                telemetry.addData("Currently at",  " at %7d", liftDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            liftDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            liftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }
    }
    */
 }

