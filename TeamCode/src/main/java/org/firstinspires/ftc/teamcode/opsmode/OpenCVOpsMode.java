package org.firstinspires.ftc.teamcode.opsmode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.opencv.Signal;
import org.firstinspires.ftc.teamcode.opencv.TestPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@TeleOp(name="OpenCV")
public class OpenCVOpsMode extends LinearOpMode implements OpenCvCamera.AsyncCameraOpenListener {

    private OpenCvCamera camera;

    @Override
    public void runOpMode() throws InterruptedException {
        TestPipeline pipeline = new TestPipeline();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        // With live preview
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        camera.openCameraDeviceAsync(this);
        camera.setPipeline(pipeline);
        waitForStart();
        while (opModeIsActive()) {
            Signal displayedSignal = pipeline.getDisplayedSignal();
            if (displayedSignal != null) {
                telemetry.addData("Color: ", displayedSignal.getColor());
            }
            telemetry.update();
        }
    }

    @Override
    public void onOpened() {
        telemetry.addData("Status", "Webcam initialized");
        telemetry.update();
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    @Override
    public void onError(int errorCode) {
        telemetry.addData("Status", "Error initializing camera");
        telemetry.update();
    }
}
