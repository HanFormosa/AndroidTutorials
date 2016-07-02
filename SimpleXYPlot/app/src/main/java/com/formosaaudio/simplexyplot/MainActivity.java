package com.formosaaudio.simplexyplot;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private XYPlot plot;
    private static final String TAG = "Plot";
    private SimpleXYSeries mySeries = null;
    private XYSeries series1 = null;
    private XYSeries series2 = null;
    private Number[] freqArray = new Number[285];
    private Number[] MagArray = new Number[285];
    private Number[] w0Array = new Number[285];
    private Number[] phiArray = new Number[285];
    private PEQ myLPF = new PEQ(500, 0.71);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simply_xy_plot_example);

        //initialise freqarray and magnitude array
        for (int i = 0; i < 285 ; i ++){
            freqArray[i] = 0;
            MagArray[i] = 0;
            w0Array[i] = 0;
            phiArray[i] = 0;
        }

        //customise input for freq array
        int x = 10;
        for (int i = 0; i < 91 ; i ++){
            freqArray[i] = x;
            x++;
        }
        Log.i(TAG, "Freq at index 90 is " + freqArray[90]);

        x = 100;
        for (int i = 0; i < 91 ; i ++){
            freqArray[i+91] = x;
            x = x + 10;
        }
        Log.i(TAG, "Freq at index 181 is " + freqArray[181]);

        x = 1000;
        for (int i = 0; i < 91 ; i ++){
            freqArray[i+91+91] = x;
            x = x + 100;
        }

        Log.i(TAG, "Freq at index 272 is " + freqArray[272]);

        //calculate w0 array based on frequency array
        for (int i = 0 ; i < 273 ; i ++){
            w0Array[i] = (freqArray[i].doubleValue() * Math.PI * 2)/PEQ.kSAMPLESIZE;
        }

        Log.i(TAG, "w0 at index 272 is " + w0Array[272]);

        //calculate phi array based on w0 array
        for (int i = 0 ; i < 273 ; i ++){
            phiArray[i] = 4 * Math.pow(Math.sin(w0Array[i].doubleValue()/2),2);
        }

        Log.i(TAG, "phi at index 272 is " + phiArray[272]);

        //calculate magnitude
        myLPF.calcCoeffs(PEQ.kLPF);
        calcFreqMag(myLPF);

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        // create a couple arrays of y-values to plot:
        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};
        Number[] series2Numbers = {5, 2, 10, 5, 20, 10, 40, 20, 80, 40};

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        series1 = new SimpleXYSeries(Arrays.asList(series1Numbers),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");

        series2 = new SimpleXYSeries(Arrays.asList(series2Numbers),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.rgb(0, 0, 200), null, null, null);
//        series2Format.setPointLabelFormatter(new PointLabelFormatter());
//        series2Format.configure(getApplicationContext(),
//                R.xml.line_point_formatter_with_labels_2);

        // add an "dash" effect to the series2 line:
        series2Format.getLinePaint().setPathEffect(
                new DashPathEffect(new float[] {

                        // always use DP when specifying pixel sizes, to keep things consistent across devices:
                        PixelUtils.dpToPix(20),
                        PixelUtils.dpToPix(15)}, 0));

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);

        // reduce the number of range labels
//        plot.setTicksPerRangeLabel(3);

        // thin out domain tick labels so they dont overlap each other:
        plot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        plot.setDomainStepValue(1);

        plot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        plot.setRangeStepValue(10);

        plot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        // rotate domain labels 45 degrees to make them more compact horizontally:
        plot.getGraphWidget().setDomainLabelOrientation(-45);

    }

    public void drawSomething(View view){
        Number[] series3Numbers = {1, 64, 64, 64, 64, 64, 64, 64, 64, 64};
        XYSeries series3 = new SimpleXYSeries(Arrays.asList(series3Numbers),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series3");
//        LineAndPointFormatter series3Format = new LineAndPointFormatter();
//        series3Format.setPointLabelFormatter(new PointLabelFormatter());
//        series3Format.configure(getApplicationContext(),
//                R.xml.line_point_formatter_with_labels_3);
//
//        series3Format.setInterpolationParams(
//                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
//        plot.addSeries(series3, series3Format);
//
//
//        plot.redraw(); //needs to be called no mater what changes done.
        plot.removeSeries(series1);
//        plot.clear();
        plot.redraw();

        //experimental functions:
        //plot.getGraphWidget().getDomainCursorPosition()
        //---GRIDLINE MOD---//
//        plot.getGraphWidget().setDomainGridLinePaint();
//        plot.getGraphWidget().setDomainSubGridLinePaint();

        //--Subtick??--//
//        plot.getGraphWidget().setDomainLabelSubTickExtension();

        //--CURSOR. what is cursor?///
//        plot.getGraphWidget().setCursorLabelPaint();
//        plot.getGraphWidget().setCursorPosition();

    }

    public void drawSomething2(View v){
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.rgb(200, 0, 200), null, null, null);
        plot.addSeries(series1,series1Format);
        plot.redraw();
    }

    public void calcFreqMag(PEQ peq){
        for (int i = 0; i < 273 ; i ++){
            MagArray[i] = 10 * Math.log10(Math.pow((peq.getCoefsB()[0]+peq.getCoefsB()[1]+peq.getCoefsB()[2]),2) +(peq.getCoefsB()[0]*peq.getCoefsB()[2]*phiArray[i].doubleValue()-(peq.getCoefsB()[1]*(peq.getCoefsB()[0]+peq.getCoefsB()[2])+4*peq.getCoefsB()[0]*peq.getCoefsB()[2]))*phiArray[i].doubleValue())
                        - 10 * Math.log10(Math.pow((peq.getCoefsA()[0]+peq.getCoefsA()[1]+peq.getCoefsA()[2]),2) +(peq.getCoefsA()[0]*peq.getCoefsA()[2]*phiArray[i].doubleValue()-(peq.getCoefsA()[1]*(peq.getCoefsA()[0]+peq.getCoefsA()[2])+4*peq.getCoefsA()[0]*peq.getCoefsA()[2]))*phiArray[i].doubleValue());
        }

        Log.i(TAG, "Coef A" +  Arrays.toString(peq.getCoefsA()));
        Log.i(TAG, "Coef B" +  Arrays.toString(peq.getCoefsB()));
        Log.i(TAG,"MagArray index  0 is " + MagArray[0]);
        Log.i(TAG,"MAGArray index 272 is " +  MagArray[272]);
    }

}
