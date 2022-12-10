package org.firstinspires.ftc.teamcode.opsmode.calibrate;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Calibrate Odometry")
public class CalibrateOdometryOpsMode extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {
        //initialization


        waitForStart();

        while (opModeIsActive()) {


            telemetry.update();

        }
    }
}
