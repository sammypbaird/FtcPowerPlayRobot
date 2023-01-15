package org.firstinspires.ftc.teamcode.opsmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opencv.SignalPipeline2;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
// for opencv
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.objects.Signal;
import org.firstinspires.ftc.teamcode.opencv.SignalPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.List;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
public class AutonomousOpsMode extends LinearOpMode implements OpenCvCamera.AsyncCameraOpenListener {

	public static double DISTANCE = 23.75; // in
	private OpenCvCamera camera;
	Signal signal;

	@Override
	public void runOpMode() throws InterruptedException {
		// init cam and pipeline
		SignalPipeline2 pipeline = new SignalPipeline2();

		//initialize camera
		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

		// Create live preview in init mode
		camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
		camera.openCameraDeviceAsync(this);
		camera.setPipeline(pipeline);

		//continually update the signal while waiting for the start button
		while(!isStarted()) {
			signal = pipeline.getSignal();
			if (signal != null) {
				telemetry.addData("Signal: ", signal.getColor());
				telemetry.addData("Hue: ", pipeline.getSignalHue());
				telemetry.update();
			}
		}

		//if for some reason we still have no signal, just go to signal 2
		if (signal == null)
			signal = Signal.TWO;

		telemetry.addData("Signal: ", signal.getColor());
		telemetry.addData("Hue: ", pipeline.getSignalHue());
		telemetry.update();

		Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
		SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

		// determine where to move
		List<Trajectory> trajectories = createTrajectoriesFromSignal(signal, drive);
		if (isStopRequested()) return;

		for (Trajectory trajectory:trajectories) {
			drive.followTrajectory(trajectory);
		}

		while (!isStopRequested() && opModeIsActive()) ;
	}

	private List<Trajectory> createTrajectoriesFromSignal(Signal signal, SampleMecanumDrive drive) {
		List<Trajectory> trajectories = new ArrayList<>();
		switch (signal) {
			// defined by locations on page 46 of Game Maunal part 2
			case ONE: // green
				trajectories.add(drive.trajectoryBuilder(new Pose2d()).forward(DISTANCE).build());
				trajectories.add(drive.trajectoryBuilder(new Pose2d()).strafeLeft(DISTANCE).build());
				break;
			case TWO: // purple
				trajectories.add(drive.trajectoryBuilder(new Pose2d()).forward(DISTANCE).build());
				break;
			case THREE: // orange
				trajectories.add(drive.trajectoryBuilder(new Pose2d()).forward(DISTANCE).build());
				trajectories.add(drive.trajectoryBuilder(new Pose2d()).strafeRight(DISTANCE).build());
				break;
		}
		return trajectories;
	}

	@Override
	public void onOpened() {
		telemetry.addData("Status", "Webcam initialized");
		telemetry.update();
		//widescreen webcams are 16:9 ratio
		camera.startStreaming(426, 240, OpenCvCameraRotation.UPRIGHT);
	}

	@Override
	public void onError(int errorCode) {
		telemetry.addData("Status", "Error initializing camera");
		telemetry.update();
	}
}
