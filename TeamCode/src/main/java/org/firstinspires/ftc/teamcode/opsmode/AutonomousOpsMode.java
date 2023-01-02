package org.firstinspires.ftc.teamcode.opsmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
// for opencv
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.opencv.Signal;
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
		SignalPipeline pipeline = new SignalPipeline();

		//initialize camera
		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

		// Create live preview in init mode
		camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
		camera.openCameraDeviceAsync(this);
		camera.setPipeline(pipeline);
		while(!isStarted())
		{
			signal = pipeline.getDisplayedSignal();
			if (signal != null) {
				telemetry.addData("Signal: ", signal.getColor());
				telemetry.update();
			}
		}

		waitForStart();

		Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
		SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

		// determine where to move
		List<Trajectory> trajectories = createTrajectoriesFromSignal(signal, drive);
		if (isStopRequested()) return;

		for (Trajectory trajectory:trajectories) {
			drive.followTrajectory(trajectory);
		}

		Pose2d poseEstimate = drive.getPoseEstimate();
		telemetry.addData("Signal: ", signal.getColor());
		telemetry.update();

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
		camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
	}

	@Override
	public void onError(int errorCode) {
		telemetry.addData("Status", "Error initializing camera");
		telemetry.update();
	}
}
