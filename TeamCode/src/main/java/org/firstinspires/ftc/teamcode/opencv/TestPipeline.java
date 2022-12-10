package org.firstinspires.ftc.teamcode.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class TestPipeline extends OpenCvPipeline
{
    //Constants
    private static final double[] SIGNAL_1_HUE = new double[]{170, 180}; //red
    private static final double[] SIGNAL_2_HUE = new double[]{25, 35}; //yellow
    private static final double[] SIGNAL_3_HUE = new double[]{108, 118}; //blue
    
    // Notice this is declared as an instance variable (and re-used), not a local variable
    Mat hsvMat = new Mat();
    Mat hierarchyMat = new Mat();

    private Signal displayedSignal = null;

    //configurations
    int erosionKernelSize = 1;
    int dilationKernelSize = 4;
    int elementType = Imgproc.CV_SHAPE_RECT;

    @Override
    public Mat processFrame(Mat input)
    {
        //convert color to HSV space
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);
        
        double largestShapeSignal1 = getLargestSizeByHue(SIGNAL_1_HUE[0], SIGNAL_1_HUE[1]);
        double largestShapeSignal2 = getLargestSizeByHue(SIGNAL_2_HUE[0], SIGNAL_2_HUE[1]);
        double largestShapeSignal3 = getLargestSizeByHue(SIGNAL_3_HUE[0], SIGNAL_3_HUE[1]);

        if (largestShapeSignal1 >= largestShapeSignal2 && largestShapeSignal1 >= largestShapeSignal3) {
            displayedSignal = Signal.ONE;
        } else if (largestShapeSignal2 >= largestShapeSignal1 && largestShapeSignal2 >= largestShapeSignal3) {
            displayedSignal = Signal.TWO;
        } else if (largestShapeSignal3 >= largestShapeSignal1 && largestShapeSignal3 >= largestShapeSignal2) {
            displayedSignal = Signal.THREE;
        }

        //combine the mask and the original image
//        Mat dest = new Mat();
//        Core.bitwise_and(input, input, dest, hsvThresholdMat);
        return input;
    }

    private double getLargestContourSize(List<MatOfPoint> contours) {
        double largestSize = 0;
        for (MatOfPoint contour : contours) {
            double size = Imgproc.contourArea(contour);
            if (size > largestSize) {
                largestSize = size;
            }
        }
        return largestSize;
    }
    
    private double getLargestSizeByHue(double hueMin, double hueMax) {
        //Define the thresholds we want to use for the color identification
        Mat hsvThresholdMat = new Mat();
        Scalar lowHSV = new Scalar(hueMin, 100, 100);
        Scalar highHSV = new Scalar(hueMax, 255, 255);

        //Apply the HSV thresholds to the image
        // We'll get a black and white image. The white regions represent matching the color.
        Core.inRange(hsvMat, lowHSV, highHSV, hsvThresholdMat);

        //erode then dilate the image
        Mat erosionElement = Imgproc.getStructuringElement(elementType, new Size(2 * erosionKernelSize + 1, 2 * erosionKernelSize + 1), new Point(erosionKernelSize, erosionKernelSize));
        Imgproc.erode(hsvThresholdMat, hsvThresholdMat, erosionElement);
        Mat dilationElement = Imgproc.getStructuringElement(elementType, new Size(2 * dilationKernelSize + 1, 2 * dilationKernelSize + 1), new Point(dilationKernelSize, dilationKernelSize));
        Imgproc.dilate(hsvThresholdMat, hsvThresholdMat, dilationElement);

        List<MatOfPoint> contourPoints = new ArrayList<>();
        Imgproc.findContours(hsvThresholdMat, contourPoints, hierarchyMat, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        return getLargestContourSize(contourPoints);
    }

    public Signal getDisplayedSignal() {
        return displayedSignal;
    }
}