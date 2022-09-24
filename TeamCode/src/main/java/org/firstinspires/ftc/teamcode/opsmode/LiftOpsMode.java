package org.firstinspires.ftc.teamcode.opsmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

// See RobotAutoDriveByEncoder_Linear for an example on how to use the encoder
@TeleOp(name="Lift")
public class LiftOpsMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor liftDrive;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        liftDrive = hardwareMap.get(DcMotor.class, "lift_drive");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        liftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        double power = 0.1;
        boolean isDirectionForward = true;
        long begin = System.currentTimeMillis();
        telemetry.addData("Starting at",  "%7d", liftDrive.getCurrentPosition());

        // run until the end of the match (driver presses STOP)
        liftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (opModeIsActive()) {

            //flip the direction after 1 second
            if (System.currentTimeMillis() - begin > 1000)
            {
                begin = System.currentTimeMillis();
                isDirectionForward = !isDirectionForward;
                power = -power;
            }

            // Send calculated power to wheels
            liftDrive.setPower(power);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Encoder at ",  "%7d", liftDrive.getCurrentPosition());
            telemetry.update();
        }
    }
}