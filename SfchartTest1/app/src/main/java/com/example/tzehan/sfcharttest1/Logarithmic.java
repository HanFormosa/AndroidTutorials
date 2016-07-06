package com.example.tzehan.sfcharttest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.syncfusion.charts.CategoryAxis;
import com.syncfusion.charts.ChartDataPoint;
import com.syncfusion.charts.ChartTrackballBehavior;
import com.syncfusion.charts.LogarithmicAxis;
import com.syncfusion.charts.NumericalAxis;
import com.syncfusion.charts.ObservableArrayList;
import com.syncfusion.charts.SfChart;
import com.syncfusion.charts.SplineSeries;
import com.syncfusion.charts.enums.TrackballLabelDisplayMode;
import com.syncfusion.charts.enums.Visibility;

import java.util.Arrays;

public class Logarithmic extends AppCompatActivity {
    private static final String TAG = "SF";
    private Number[] freqArray = new Number[273];
    private Number[] MagArray = new Number[273];
    private Number[] w0Array = new Number[273];
    private Number[] phiArray = new Number[273];
    private PEQ myLPF = new PEQ(200, 0.71);

    DataModel dataModel  =  new DataModel  ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logarithmic);

        final SfChart chart = (SfChart)findViewById(R.id.logsfChart);
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


        //calculate magnitude
        myLPF.calcCoeffs(PEQ.kLPF);
        calcFreqMag(myLPF);

        //Initializing chart
        chart.getTitle().setText("Frequency Response");

        //Initializing Primary Axis
        LogarithmicAxis logarithmic = new LogarithmicAxis();

        logarithmic.setMinorTicksPerInterval(5);
        logarithmic.setShowMinorGridLines(true);
//        logarithmic.setShowMajorGridLines(true);
        chart.setPrimaryAxis(logarithmic);

        chart.getPrimaryAxis().getTitle().setText("Frequency");

        //Initializing Secondary Axis

        NumericalAxis numeric = new NumericalAxis();
//        numeric.setMinorTicksPerInterval(5);
//        numeric.setShowMinorGridLines(true);
        numeric.setMaximum(10);
        numeric.setMinimum(-20);
        chart.setSecondaryAxis(numeric);

//        chart.getSecondaryAxis().getTitle().setText("dB");

        SplineSeries seriesLow = new SplineSeries();

        seriesLow.setDataSource(dataModel.MagLPF);

        seriesLow.setLabel("LPF");
        seriesLow.setDataPointSelectionEnabled(true);
//        seriesLow.setSelectedDataPointIndex(10);//can't be put after add series.
        chart.getSeries().add(seriesLow);

        //Adding Chart Legend for the Chart

        chart.getLegend().setVisibility(Visibility.Visible);

        ChartTrackballBehavior trackballBehavior = new ChartTrackballBehavior();
        trackballBehavior.setShowLabel(true);
        trackballBehavior.setShowLine(true);
        trackballBehavior.setLabelDisplayMode(TrackballLabelDisplayMode.NearestPoint);
        chart.getBehaviors().add(trackballBehavior);



        /**
         * configure data here.
         */
        dataModel.MagLPF.clear();
        for (int i =0; i < 181; i++){
            Comparable f = ((Comparable) freqArray[i]);
            dataModel.MagLPF.add(new ChartDataPoint  (f,  MagArray[i]));
        }


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

    public class DataModel{

        public ObservableArrayList MagLPF;

        public DataModel(){
/**
 * this data is not important, just dummy, real thing happen in OnCreate
 */
            MagLPF = new ObservableArrayList();

//            for (int i =0; i < 50; i++){
//                Comparable f = ((Comparable) freqArray[i]);
//                MagLPF.add(new ChartDataPoint  (f,  0));
//            }
            Comparable f0  = ((Comparable) freqArray[0]);
            Number m0 =  MagArray[0];
            MagLPF.add(new ChartDataPoint  (10,  0));
            Comparable f1  = ((Comparable) freqArray[50]);
            MagLPF.add(new ChartDataPoint  (100,  0));

            MagLPF.add(new ChartDataPoint  (1000,  -12.3));

            MagLPF.add(new ChartDataPoint  (10000,  -54.6));

//            MagLPF.add(new ChartDataPoint  ("May",  75));
//
//            MagLPF.add(new ChartDataPoint  ("Jun",  83));
//
//            MagLPF.add(new ChartDataPoint  ("Jul",  87));

        }
    }

    public void changeFreq (View v){

        double freq = myLPF.getFreq();
        freq = freq+10;
        myLPF.setFreq(freq);
        //calculate magnitude
        myLPF.calcCoeffs(PEQ.kLPF);
        calcFreqMag(myLPF);
        dataModel.MagLPF.clear();
        for (int i =0; i < 181; i++){
            Comparable f = ((Comparable) freqArray[i]);
            dataModel.MagLPF.add(new ChartDataPoint  (f,  MagArray[i]));
        }

    }
}
