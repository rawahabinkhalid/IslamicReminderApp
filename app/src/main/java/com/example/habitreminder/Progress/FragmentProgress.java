package com.example.habitreminder.Progress;


import android.graphics.Color;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontStyle;
import android.icu.text.DecimalFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.habitreminder.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProgress extends Fragment {

    PieChart pieChart;
    PieChart pieChartStreaks;
    String reminder_completed_text;
    String streaks_completed_text;
    public FragmentProgress() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Rootview = null;
        Rootview = inflater.inflate(R.layout.fragment_progress, container,false);
        // Inflate the layout for this fragment
        pieChart = Rootview.findViewById(R.id.remindersCompleted);
        pieChartStreaks = Rootview.findViewById(R.id.streaksCompleted);
        piechartmethod();
        streakchartmethod();
        return Rootview;
    }

    private void streakchartmethod() {
        pieChartStreaks.setUsePercentValues(false);
        pieChartStreaks.getDescription().setEnabled(false);
        pieChartStreaks.setExtraOffsets(5,10,5,5);

        pieChartStreaks.setDragDecelerationFrictionCoef(0.95f);

        pieChartStreaks.setDrawHoleEnabled(true);
        pieChartStreaks.setHoleColor(Color.WHITE);
        pieChartStreaks.setTransparentCircleRadius(0);
        streaks_completed_text =getString(R.string.current_streaks);
        SpannableString ss = new SpannableString(streaks_completed_text);

        ForegroundColorSpan fcsgreen = new ForegroundColorSpan(Color.parseColor("#112331"));
        ForegroundColorSpan fcspink = new ForegroundColorSpan(Color.parseColor("#FFCFB5"));
        ss.setSpan(fcsgreen, 0,14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(FontStyle.FONT_WEIGHT_MAX, 15,18,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcspink, 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChartStreaks.setCenterText(ss);
        pieChartStreaks.setCenterTextSize(18);
        pieChartStreaks.setCenterTextColor(Color.parseColor("#113221"));
        pieChartStreaks.setHoleRadius(80);
        pieChartStreaks.getLegend().setEnabled(false);

        ArrayList<PieEntry> Val = new ArrayList<>();

        Val.add(new PieEntry(30,""));
        Val.add(new PieEntry(70,""));
        final int[] MY_COLORS = {Color.rgb(191, 239, 187), Color.rgb(230,230,230)};
        PieDataSet dataSet = new PieDataSet(Val,"Reminder Completed");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(MY_COLORS);
        PieData data = new PieData(dataSet);
//        data.setValueTextSize(10f);
//        data.setValueTextColor(Color.YELLOW);
        data.setValueTextSize(0);


        pieChartStreaks.setData(data);
    }

    private void piechartmethod()
    {

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(0);
        reminder_completed_text =getString(R.string.pie_chart_reminder_heading);
        SpannableString ss = new SpannableString(reminder_completed_text);

        ForegroundColorSpan fcsgreen = new ForegroundColorSpan(Color.parseColor("#112331"));
        ForegroundColorSpan fcspink = new ForegroundColorSpan(Color.parseColor("#FFCFB5"));
        ss.setSpan(fcsgreen, 0,18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(FontStyle.FONT_WEIGHT_MAX, 19,22,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcspink, 19, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChart.setCenterText(ss);
        pieChart.setCenterTextSize(18);
        pieChart.setCenterTextColor(Color.parseColor("#113221"));
        pieChart.setHoleRadius(80);
        pieChart.getLegend().setEnabled(false);

        ArrayList<PieEntry> Val = new ArrayList<>();

        Val.add(new PieEntry(30,""));
        Val.add(new PieEntry(70,""));
        final int[] MY_COLORS = {Color.rgb(254,207,181), Color.rgb(230,230,230)};
        PieDataSet dataSet = new PieDataSet(Val,"Reminder Completed");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(MY_COLORS);
        PieData data = new PieData(dataSet);
//        data.setValueTextSize(10f);
//        data.setValueTextColor(Color.YELLOW);
        data.setValueTextSize(0);


        pieChart.setData(data);

    }

}
