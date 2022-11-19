package org.firstinspires.ftc.teamcode.sandbox.kavya;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp(name="Calibrate Servo Kavya")
public class CalibrateServoOpsMode extends LinearOpMode {
    Servo claw;
    int servoPosition = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        //initialization
        claw = hardwareMap.get(Servo.class, "claw");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_up)
                servoPosition += 0.01;
            else if (gamepad1.dpad_down)
                servoPosition -= 0.01;

            if (servoPosition > 1)
                servoPosition = 1;
            if (servoPosition < -1)
                servoPosition = -1;

            claw.setPosition(servoPosition);
            telemetry.addData("Claw Position: ", "%7d", claw.getPosition());

        }
    }
}
