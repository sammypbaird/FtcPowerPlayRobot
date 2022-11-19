package org.firstinspires.ftc.teamcode.opsmode.calibrate;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Calibrate Claw")
public class CalibrateClawOpsMode extends LinearOpMode {

    Servo claw;
    double servoPosition = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        //initialization
        claw = hardwareMap.get(Servo.class, "claw");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_up)
                servoPosition += 0.001;
            else if (gamepad1.dpad_down)
                servoPosition -= 0.001;

            claw.setPosition(servoPosition);
            telemetry.addData("Claw Position: ", "%4.2f", claw.getPosition());
            telemetry.update();

        }
    }
}
