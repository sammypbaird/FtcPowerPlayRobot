package org.firstinspires.ftc.teamcode.opsmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

// See RobotAutoDriveByEncoder_Linear for an example on how to use the encoder
@TeleOp(name="Lift Encoder")
public class LiftOpsMode extends LinearOpMode {

    private static final int COUNTS_PER_INCH = 1000; //refine this

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor liftDrive;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        liftDrive = hardwareMap.get(DcMotor.class, "lift_drive");
        liftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Starting at",  "%7d", liftDrive.getCurrentPosition());

        waitForStart();
        runtime.reset();

        for (int i=0; i<5; i++) {
            encoderDrive(0.1*(i+2), 1000, 5);
            encoderDrive(0.1*(i+2), 0, 5);
        }
    }


    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
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
}