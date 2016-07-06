package com.example.tzehan.sfcharttest1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.syncfusion.charts.CategoryAxis;
import com.syncfusion.charts.ChartDataPoint;
import com.syncfusion.charts.ChartTrackballBehavior;
import com.syncfusion.charts.ColumnSeries;
import com.syncfusion.charts.NumericalAxis;
import com.syncfusion.charts.ObservableArrayList;
import com.syncfusion.charts.SfChart;
import com.syncfusion.charts.SplineAreaSeries;
import com.syncfusion.charts.SplineSeries;
import com.syncfusion.charts.enums.TrackballLabelDisplayMode;
import com.syncfusion.charts.enums.Visibility;

public class MainActivity extends AppCompatActivity {

    DataModel dataModel  =  new DataModel  ();
    private static final String TAG = "SF";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final SfChart chart = (SfChart)findViewById(R.id.sfchart);
        //Initializing chart

        chart.getTitle().setText("Weather Analysis");
        ChartTrackballBehavior trackballBehavior = new ChartTrackballBehavior();
        trackballBehavior.setShowLabel(true);
        trackballBehavior.setShowLine(true);
        trackballBehavior.setLabelDisplayMode(TrackballLabelDisplayMode.NearestPoint);
        chart.getBehaviors().add(trackballBehavior);

        //Initializing Primary Axis

        chart.setPrimaryAxis(new CategoryAxis());

        chart.getPrimaryAxis().getTitle().setText("Month");

        //Initializing Secondary Axis

        chart.setSecondaryAxis(new NumericalAxis());

        chart.getSecondaryAxis().getTitle().setText("Temperature");



        //Adding ColumnSeries to the chart for Precipitation

        ColumnSeries series = new ColumnSeries();

        series.setDataSource(dataModel.Precipitation);

        series.setLabel("Precipitation");

        NumericalAxis numeric = new NumericalAxis();

        numeric.setOpposedPosition(true);

        numeric.setShowMajorGridLines(true);
        //set axis boundary
        numeric.setMaximum(5);
        numeric.setMinimum(0);

        series.setYAxis(numeric);
        series.setDataPointSelectionEnabled(true);
        series.setSelectedDataPointIndex(3);
        chart.getSeries().add(series);

        //Adding the SplineSeries to the chart for high temperature

        SplineSeries seriesHigh = new SplineSeries();

        seriesHigh.setDataSource(dataModel.HighTemperature);

        seriesHigh.setLabel("High");

        NumericalAxis numeric2 = new NumericalAxis();
        numeric2.setMaximum(90);
        numeric2.setMinimum(0);
        chart.setSecondaryAxis(numeric2);
        dataModel.HighTemperature.setOnCollectionChangedListener(new ObservableArrayList.OnCollectionChangedListener() {
            @Override
            public void onAddIndex(int i, Object o) {
                Log.i(TAG, "series high on add index");
            }

            @Override
            public void onAddObject(Object o) {
                Log.i(TAG, "series high on add object");
            }

            @Override
            public void onRemoveIndex(int i, Object o) {
                Log.i(TAG, "series high on remove index");
            }

            @Override
            public void onRemoveObject(Object o) {
                Log.i(TAG, "series high on remove object");
            }

            @Override
            public void onClearList() {
                Log.i(TAG, "series high cleared");
            }

            @Override
            public void onAddAll(int i) {
                Log.i(TAG, "series high on Add all");
            }

            @Override
            public void onSetObject(int i, Object o) {
                Log.i(TAG, "series high on set object");
            }
        });
        seriesHigh.setStrokeWidth(10.0f);
        chart.getSeries().add(seriesHigh);

        //Adding the SplineSeries to the chart for low temperature

        SplineAreaSeries seriesLow = new SplineAreaSeries();

        seriesLow.setDataSource(dataModel.LowTemperature);

        seriesLow.setLabel("Low");
        int greenColorValue = Color.GREEN;
        seriesLow.setDataPointSelectionEnabled(true);
        seriesLow.setSelectedDataPointIndex(1);
        seriesLow.setColor(greenColorValue);
        seriesLow.setAlpha(0.5f);
        seriesLow.setStrokeWidth(10.0f);
        chart.getSeries().add(seriesLow);

        //Adding Chart Legend for the Chart

        chart.getLegend().setVisibility(Visibility.Visible);



    }

    public void onClick(View v){
        SfChart chart = (SfChart)findViewById(R.id.sfchart);
//        chart.getSeries().removeAll(dataModel.HighTemperature);
        dataModel.HighTemperature.clear();
        chart.redrawChart();
    }

    public void onClick2(View v){
        SfChart chart = (SfChart)findViewById(R.id.sfchart);
//        chart.getSeries().removeAll(dataModel.HighTemperature);
//        SplineSeries seriesHigh = new SplineSeries();
//        seriesHigh.setDataSource(dataModel.HighTemperature);
//        seriesHigh.setLabel("High");
//        chart.getSeries().add(seriesHigh);


//        chart.reloadChart();
//        chart.redrawChart();
        dataModel.HighTemperature.add(new ChartDataPoint  ("Jan",  42));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Feb",  44));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Mar",  53));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Apr",  64));

        dataModel.HighTemperature.add(new ChartDataPoint  ("May",  75));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Jun",  83));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Jul",  87));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Aug",  84));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Sep",  78));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Oct",  67));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Nov",  55));

        dataModel.HighTemperature.add(new ChartDataPoint  ("Dec",  45));

    }


public class DataModel {

    public ObservableArrayList  HighTemperature;

    public ObservableArrayList  LowTemperature;

    public ObservableArrayList  Precipitation;

    public DataModel  () {

        HighTemperature  =  new ObservableArrayList  ();

        HighTemperature.add(new ChartDataPoint  ("Jan",  42));

        HighTemperature.add(new ChartDataPoint  ("Feb",  44));

        HighTemperature.add(new ChartDataPoint  ("Mar",  53));

        HighTemperature.add(new ChartDataPoint  ("Apr",  64));

        HighTemperature.add(new ChartDataPoint  ("May",  75));

        HighTemperature.add(new ChartDataPoint  ("Jun",  83));

        HighTemperature.add(new ChartDataPoint  ("Jul",  87));

        HighTemperature.add(new ChartDataPoint  ("Aug",  84));

        HighTemperature.add(new ChartDataPoint  ("Sep",  78));

        HighTemperature.add(new ChartDataPoint  ("Oct",  67));

        HighTemperature.add(new ChartDataPoint  ("Nov",  55));

        HighTemperature.add(new ChartDataPoint  ("Dec",  45));

        LowTemperature  =  new ObservableArrayList  ();

        LowTemperature.add(new ChartDataPoint  ("Jan",  27));

        LowTemperature.add(new ChartDataPoint  ("Feb",  28));

        LowTemperature.add(new ChartDataPoint  ("Mar",  35));

        LowTemperature.add(new ChartDataPoint  ("Apr",  44));

        LowTemperature.add(new ChartDataPoint  ("May",  54));

        LowTemperature.add(new ChartDataPoint  ("Jun",  63));

        LowTemperature.add(new ChartDataPoint  ("Jul",  68));

        LowTemperature.add(new ChartDataPoint  ("Aug",  66));

        LowTemperature.add(new ChartDataPoint  ("Sep",  59));

        LowTemperature.add(new ChartDataPoint  ("Oct",  48));

        LowTemperature.add(new ChartDataPoint  ("Nov",  38));

        LowTemperature.add(new ChartDataPoint  ("Dec",  29));

        Precipitation  =  new ObservableArrayList  ();

        Precipitation.add(new ChartDataPoint  ("Jan",  3.03));

        Precipitation.add(new ChartDataPoint  ("Feb",  2.48));

        Precipitation.add(new ChartDataPoint  ("Mar",  3.23));

        Precipitation.add(new ChartDataPoint  ("Apr",  3.15));

        Precipitation.add(new ChartDataPoint  ("May",  4.13));

        Precipitation.add(new ChartDataPoint  ("Jun",  3.23));

        Precipitation.add(new ChartDataPoint  ("Jul",  4.13));

        Precipitation.add(new ChartDataPoint  ("Aug",  4.88));

        Precipitation.add(new ChartDataPoint  ("Sep",  3.82));

        Precipitation.add(new ChartDataPoint  ("Oct",  3.07));

        Precipitation.add(new ChartDataPoint  ("Nov",  2.83));

        Precipitation.add(new ChartDataPoint  ("Dec",  2.8));
    }
}

    public class DataModel2{
        public ObservableArrayList  FreqMag;

    }
}

