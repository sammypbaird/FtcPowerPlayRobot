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
import org.firstinspires.ftc.teamcode.opencv.TestPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
public class AutonomousOpsMode extends LinearOpMode {
    public static double DISTANCE = 23.75; // in
    private OpenCvCamera camera;
    @Override
    public void runOpMode() throws InterruptedException {
    	// init cam and pipeline
    	TestPipeline pipeline = new TestPipeline();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
	// get color 
	Signal displayedSignal = pipeline.getDisplayedSignal();
	// determine where to move 
	switch(displayedSignal.getColor()){
	// defined by locations on page 46 of Game Maunal part 2
		case 1: // red
			Trajectory forwardTrajectory = drive.trajectoryBuilder(new Pose2d())
				.forward(DISTANCE)
				.build();
			Trajectory strafeTrajectory = drive.trajectoryBuilder(new Pose2d())
				.strafeLeft(DISTANCE)
				.build();
			break;
		case 2: // yellow
			Trajectory forwardTrajectory = drive.trajectoryBuilder(new Pose2d())
				.forward(DISTANCE)
				.build();
			break;
		case 3: // blue
			Trajectory forwardTrajectory = drive.trajectoryBuilder(new Pose2d())
				.forward(DISTANCE)
				.build();
			Trajectory strafeTrajectory = drive.trajectoryBuilder(new Pose2d())
				.strafeRight(DISTANCE)
				.build();
			break;
	}
        
        

        waitForStart();

        if (isStopRequested()) return;

        drive.followTrajectory(forwardTrajectory);
        if(strafeTrajectory != NULL)
        	drive.followTrajectory(strafeTrajectory);

        Pose2d poseEstimate = drive.getPoseEstimate();
        telemetry.addData("finalX", poseEstimate.getX());
        telemetry.addData("finalY", poseEstimate.getY());
        telemetry.addData("finalHeading", poseEstimate.getHeading());
        telemetry.update();

        while (!isStopRequested() && opModeIsActive()) ;
    }
}
