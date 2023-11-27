package com.example.quiz_diadanh;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;

public class ChartFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart, container, false);

        BarChart barChart = view.findViewById(R.id.barChart);
        setupChart(barChart);

        return view;
    }

    private void setupChart(BarChart chart) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 5f));
        entries.add(new BarEntry(2, 8f));
        entries.add(new BarEntry(3, 12f));
        entries.add(new BarEntry(4, 6f));

        BarDataSet dataSet = new BarDataSet(entries, "Label");
        BarData barData = new BarData(dataSet);

        chart.setData(barData);
        chart.invalidate();
    }
}
