package com.example.tzehan.xylogscatter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.xy.*;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Scatter";
    private XYPlot plot;
    private XYSeries magSeries = null;
    private Number[] freqArray = new Number[273];
    private Number[] MagArray = new Number[273];
    private Number[] w0Array = new Number[273];
    private Number[] phiArray = new Number[273];
    private PEQ myLPF = new PEQ(2000, 0.71);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scatter_plot_example);

        //initialise freqarray and magnitude array
        for (int i = 0; i < 273 ; i ++){
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

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        //calculate magnitude
        myLPF.calcCoeffs(PEQ.kLPF);
        calcFreqMag(myLPF);

        magSeries = generateScatter("series1",273, new RectRegion(10,10000,-60,10));

//        XYSeries series1 = generateScatter("series1", 80, new RectRegion(10, 50, 10, 50));
//        XYSeries series2 = generateScatter("series2", 80, new RectRegion(30, 70, 30, 70));

        plot.setDomainBoundaries(10, 5000, BoundaryMode.FIXED);
        plot.setRangeBoundaries(-40, 10, BoundaryMode.FIXED);

//        plot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
//        plot.setDomainStepValue(Math.E);

        plot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        plot.setRangeStepValue(10);

        LineAndPointFormatter magSeriesFormat = new LineAndPointFormatter(Color.rgb(0, 0, 200), null, null, null);
        LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.rgb(200, 0, 0), null, null, null);

        // add each series to the xyplot:
        plot.addSeries(magSeries, magSeriesFormat);
//        plot.addSeries(series2, series2Format);

        // reduce the number of range labels
//        plot.setTicksPerRangeLabel(10);

        // rotate domain labels 45 degrees to make them more compact horizontally:
        plot.getGraphWidget().setDomainLabelOrientation(-45);
    }

    /**
     * Generate a XYSeries of random points within a specified region
     * @param title
     * @param numPoints
     * @param region
     * @return
     */
    private XYSeries generateScatter(String title, int numPoints, RectRegion region) {
        SimpleXYSeries series = new SimpleXYSeries(title);
        for(int i = 0; i < numPoints; i++) {
            series.addLast(
                    freqArray[i],
                    MagArray[i]
            );
        }
        return series;
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
