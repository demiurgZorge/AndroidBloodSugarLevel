package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class GraphListenerImpl implements IUiUpdateListListener<SugarDto> {

    GraphView graph;

    public GraphListenerImpl(GraphView graph) {
        super();
        this.graph = graph;
    }

    private void setGraphWithSugarLevelData(GraphView graph, List<SugarDto> listDto) {
        graph.clearSecondScale();
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (SugarDto dto : listDto) {
            series.appendData(new DataPoint(dto.date, dto.level), true, listDto.size());
        }
        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(listDto.size() - 1);
//
////        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(listDto.get(0).date.getTime());
        graph.getViewport().setMaxX(listDto.get(listDto.size() - 1).date.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
//
//        graph.getViewport().setMinY(0.0f);
//        graph.getViewport().setMaxY(30.0);
//        graph.getGridLabelRenderer().setNumVerticalLabels(5);
//
        graph.getGridLabelRenderer().setHumanRounding(false);

    }

    @Override
    public void onResponse(List<SugarDto> list) {
        setGraphWithSugarLevelData(graph, list);
    }
}
