package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.teamcode.objects.Signal;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class SignalPipeline2 extends OpenCvPipeline
{

    // Notice this is declared as an instance variable (and re-used), not a local variable
    Mat hsvMat = new Mat();
    Mat hierarchyMat = new Mat();
    private Signal signal = null;
    private double signalHue = 0;


    @Override
    public Mat processFrame(Mat input)
    {
        int size = 30;
        Rect square = new Rect(((input.width() - size) / 2), (input.height() - size) / 2, size, size);

        //blur the image
        Mat blurredImage = new Mat();
        Imgproc.GaussianBlur(input, blurredImage, new Size(10, 10), 0);

        //Draw a rectangle on the input image (just to help in previewing the image)
        Imgproc.rectangle(input, square, new Scalar(0, 255, 0), 1, 8, 0);

        //convert it to HSV
        Imgproc.cvtColor(blurredImage, hsvMat, Imgproc.COLOR_RGB2HSV);

        //get the pixel in the middle
        double[] hsvValues = hsvMat.get(input.height() / 2, input.width() / 2);
        signalHue = hsvValues[0];

        //find the closest hue
        signal = getClosestSignal(signalHue);

        return input;
    }

    private Signal getClosestSignal(double signalHue) {
        double closestHueDifference = 180;
        Signal closestSignal = Signal.ONE;
        for (Signal s : Signal.values()) {
            double diff = Math.abs(s.getHue() - signalHue);
            if (diff < closestHueDifference) {
                closestHueDifference = diff;
                closestSignal = s;
            }
        }
        return closestSignal;
    }

    public Signal getSignal() {
        return signal;
    }

    public double getSignalHue() {
        return signalHue;
    }
}