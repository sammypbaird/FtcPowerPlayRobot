package org.firstinspires.ftc.teamcode.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class TestPipeline extends OpenCvPipeline
{
    // Notice this is declared as an instance variable (and re-used), not a local variable
    Mat hsvMat = new Mat();


    //configurations
    int erosionKernelSize = 1;
    int dilationKernelSize = 4;
    int elementType = Imgproc.CV_SHAPE_RECT;

    @Override
    public Mat processFrame(Mat input)
    {
        //convert color to HSV space
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);

        //Define the thresholds we want to use for the color identification
        Mat hsvThresholdMat = new Mat();
        Scalar lowHSV = new Scalar(170, 100, 100);
        Scalar highHSV = new Scalar(180, 255, 255);

        //Apply the HSV thresholds to the image
        // We'll get a black and white image. The white regions represent matching the color.
        Core.inRange(hsvMat, lowHSV, highHSV, hsvThresholdMat);

        //erode then dilate the image
        Mat erosionElement = Imgproc.getStructuringElement(elementType, new Size(2 * erosionKernelSize + 1, 2 * erosionKernelSize + 1), new Point(erosionKernelSize, erosionKernelSize));
        Imgproc.erode(hsvThresholdMat, hsvThresholdMat, erosionElement);
        Mat dilationElement = Imgproc.getStructuringElement(elementType, new Size(2 * dilationKernelSize + 1, 2 * dilationKernelSize + 1), new Point(dilationKernelSize, dilationKernelSize));
        Imgproc.dilate(hsvThresholdMat, hsvThresholdMat, dilationElement);

        //combine the mask and the original image
        Mat dest = new Mat();
        Core.bitwise_and(input, input, dest, hsvThresholdMat);
        return dest;
    }
}