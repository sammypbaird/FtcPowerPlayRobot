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

    @Override
    public void runOpMode() throws InterruptedException {
        //initialization
        claw = hardwareMap.get(Servo.class, "claw");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        double clawPosition = claw.getPosition();

        while (opModeIsActive()) {
            telemetry.addData("Claw Position: ", "%7d", clawPosition);
        }
    }
}
