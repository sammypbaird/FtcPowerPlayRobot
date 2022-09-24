package org.firstinspires.ftc.teamcode.opsmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

// See RobotAutoDriveByEncoder_Linear for an example on how to use the encoder
@TeleOp(name="Lift Calibration")
public class LiftCalibrationOpsMode extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor liftDrive;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        liftDrive = hardwareMap.get(DcMotor.class, "lift_drive");
        liftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Starting at",  "%7d", liftDrive.getCurrentPosition());
        telemetry.update();

        waitForStart();
        while (opModeIsActive())
        {
            telemetry.addData("Currently at",  " at %7d", liftDrive.getCurrentPosition());
            telemetry.update();
        }
    }
}