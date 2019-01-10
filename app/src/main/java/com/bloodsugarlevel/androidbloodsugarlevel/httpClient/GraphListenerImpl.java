package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.DateFormat;

import com.bloodsugarlevel.androidbloodsugarlevel.common.Graph.ValueFormatter;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener.DATE_PICKER_FORMAT;
import static com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener.TIME_PICKER_FORMAT;

public class GraphListenerImpl implements IUiUpdateListListener<SugarDto> {

    LineChart graph;

    public GraphListenerImpl(LineChart graph) {
        super();
        this.graph = graph;
    }

    private void setGraphWithSugarLevelData(LineChart lineChart, List<SugarDto> listDto) {
//        graph.clearSecondScale();
//        graph.removeAllSeries();
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
//        for (SugarDto dto : listDto) {
//            series.appendData(new DataPoint(dto.date, dto.level), true, listDto.size());
//        }
//        graph.addSeries(series);
//
//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(listDto.size() - 1);
////
//////        // set manual x bounds to have nice steps
//        graph.getViewport().setMinX(listDto.get(0).date.getTime());
//        graph.getViewport().setMaxX(listDto.get(listDto.size() - 1).date.getTime());
//        graph.getViewport().setXAxisBoundsManual(true);
////
////        graph.getViewport().setMinY(0.0f);
////        graph.getViewport().setMaxY(30.0);
////        graph.getGridLabelRenderer().setNumVerticalLabels(5);
////
//        graph.getGridLabelRenderer().setHumanRounding(false);


        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        lineChart.setBackgroundColor(Color.rgb(120,15,16));
        lineChart.getDescription().setEnabled(true);
        int i = 1;
        for (SugarDto dto : listDto) {
            Entry entry = new Entry(dto.date.getTime(), dto.level);
            entries.add(entry);
        }
        LineDataSet dataset = new LineDataSet(entries , "Sugar");
        LineData data = new LineData(dataset);
        lineChart.setData(data);
        lineChart.animateX(1500);
        lineChart.setScaleXEnabled(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTypeface(Typeface.SANS_SERIF);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(0.1f); // one hour
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                return DateFormat.format( "yyyy-MM-dd hh:mm", new Date((long) value)).toString();
            }
        });
    }

    @Override
    public void onResponse(List<SugarDto> list) {
        setGraphWithSugarLevelData(graph, list);
    }
}
