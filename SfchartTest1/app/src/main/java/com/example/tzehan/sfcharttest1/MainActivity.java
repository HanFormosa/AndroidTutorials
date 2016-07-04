package com.example.tzehan.sfcharttest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.syncfusion.charts.SfChart;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SfChart sfChart = new SfChart(this);

    }
}
