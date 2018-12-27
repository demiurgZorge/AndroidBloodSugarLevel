package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

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

        int i = 1;
        for (SugarDto dto : listDto) {
            entries.add(new Entry(dto.date.getTime(), dto.level));
        }
        LineDataSet dataset = new LineDataSet(entries , "Sugar");
        LineData data = new LineData(dataset);
        lineChart.setData(data);
    }

    @Override
    public void onResponse(List<SugarDto> list) {
        setGraphWithSugarLevelData(graph, list);
    }
}
