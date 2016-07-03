package com.numetriclabz.logarithmicbarchart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.LogarithmicBarChart;
import com.numetriclabz.numandroidcharts.LogarithmicLineChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogarithmicBarChart barChart = (LogarithmicBarChart) findViewById(R.id.chart);

        LogarithmicLineChart lineChart ;
        List<ChartData> value1 = new ArrayList<>();

        //values.add(new ChartData(y,x));
        value1.add(new ChartData(5000, 2005));
        value1.add(new ChartData(9000, 206));
        value1.add(new ChartData(12000, 2007));
        value1.add(new ChartData(18000, 2008));
        value1.add(new ChartData(20000, 2009));
        value1.add(new ChartData(400000, 2010));

        List<ChartData> dataSet = new ArrayList<>();
        dataSet.add(new ChartData(value1));
        
        barChart.setData(dataSet);
        barChart.setBase(10);
    }
}
