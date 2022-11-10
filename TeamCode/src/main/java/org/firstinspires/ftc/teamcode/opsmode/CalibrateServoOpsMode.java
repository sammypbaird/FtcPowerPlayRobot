package org.firstinspires.ftc.teamcode.opsmode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp(name="Calibrate Servo")
public class CalibrateServoOpsMode extends LinearOpMode {

    Servo leftClaw;
    Servo rightClaw;

    @Override
    public void runOpMode() throws InterruptedException {
        //initialization
        leftClaw = hardwareMap.get(Servo.class, "claw_left");
        rightClaw = hardwareMap.get(Servo.class, "claw_right");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        double leftClawPosition = leftClaw.getPosition();
        double rightClawPosition = rightClaw.getPosition();

        while (opModeIsActive()) {
            telemetry.addData("Left claw: ", "%7d", leftClawPosition);
            telemetry.addData("Right claw: ", "%7d", rightClawPosition);
        }

    }
}
