package org.firstinspires.ftc.teamcode.sandbox.sarah;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.objects.Signal;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

import java.util.ArrayList;
import java.util.List;

@Autonomous(group = "drive")
public class SarahOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //Create the mecanum drive class, which manages driving the robot
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        //Create the instructions for odometry
        List<Trajectory> trajectories = createTrajectories(drive);

        //Perform all the odometry instructions, one at a time
        for (Trajectory trajectory : trajectories) {
            drive.followTrajectory(trajectory);
        }

        // don't finish the program until the stop button is pushed
        while (!isStopRequested() && opModeIsActive()) ;
    }

    private List<Trajectory> createTrajectories(SampleMecanumDrive drive) {
        List<Trajectory> trajectories = new ArrayList<>();
        trajectories.add(drive.trajectoryBuilder(new Pose2d()).forward(24).build()); //25 inches
        return trajectories;
    }
}
