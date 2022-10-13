package org.firstinspires.ftc.teamcode.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class CalibratePipeline extends OpenCvPipeline {

    // Notice this is declared as an instance variable (and re-used), not a local variable
    Mat hsvMat = new Mat();

    double[] values = new double[3];

    @Override
    public Mat processFrame(Mat input)
    {
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);

        int size = 30;
        Rect rect = new Rect(((input.width() - size) / 2), (input.height() - size) / 2, size, size);
        Imgproc.rectangle(input, rect, new Scalar(0, 255, 0), 1, 8, 0);
        values = hsvMat.get(input.height() / 2, input.width() / 2);
        return input;
    }

    public double[] getHSVValues()
    {
        return values;
    }

    public Mat getMat()
    {
        return hsvMat;
    }
}
