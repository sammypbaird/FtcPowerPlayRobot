package org.firstinspires.ftc.teamcode.opsmode.calibrate;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.opencv.CalibratePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Disabled
@TeleOp(name="OpenCV - Calibrate")
public class CalibrateOpenCVOpsMode extends LinearOpMode implements OpenCvCamera.AsyncCameraOpenListener {

    private OpenCvCamera camera;
    private CalibratePipeline calibratePipeline = new CalibratePipeline();

    @Override
    public void runOpMode() throws InterruptedException {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        // With live preview
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        camera.openCameraDeviceAsync(this);
        camera.setPipeline(calibratePipeline);
        waitForStart();
        while (opModeIsActive())
        {
            double[] hsvValues = calibratePipeline.getHSVValues();
            telemetry.addData("HSV: ",  "%.3f, %.3f, %.3f", hsvValues[0], hsvValues[1], hsvValues[2]);
            if (calibratePipeline.getMat() != null) {
                telemetry.addData("Info on Mat: %d", calibratePipeline.getMat().type());
            }
            telemetry.update();
        }

    }

    @Override
    public void onOpened()
    {
        telemetry.addData("Status", "Webcam initialized");
        telemetry.update();
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    @Override
    public void onError(int errorCode)
    {
        telemetry.addData("Status", "Error initializing camera");
        telemetry.update();
    }
}
