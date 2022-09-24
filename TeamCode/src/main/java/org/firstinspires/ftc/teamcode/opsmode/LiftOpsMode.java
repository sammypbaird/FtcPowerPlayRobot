package org.firstinspires.ftc.teamcode.opsmode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Basic: Linear OpMode", group="Linear Opmode")
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

        double power = 0.1;
        boolean isDirectionForward = true;
        long begin = System.currentTimeMillis();

        // run until the end of the match (driver presses STOP)
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
            telemetry.addData("Motor", "lift (%.2f)", liftDrive);
            telemetry.update();
        }
    }
}