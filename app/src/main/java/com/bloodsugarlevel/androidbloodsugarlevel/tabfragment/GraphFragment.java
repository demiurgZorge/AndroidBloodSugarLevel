package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.common.ApiListResult;
import com.bloodsugarlevel.common.FilterState;
import com.bloodsugarlevel.common.PagingState;
import com.bloodsugarlevel.common.QueryState;
import com.bloodsugarlevel.common.SortState;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GraphFragment extends Fragment implements Button.OnClickListener{
    public static final String HTTP_VOLEY_TAG = "HTTP_VOLEY_TAG";
    private static final long MIN_CLICK_INTERVAL=600;
    private long mLastClickTime = SystemClock.uptimeMillis();
    View mFragmentView;
    static RequestQueue mRequestQueue;

    @Override
    public void onClick(View view1) {
        long currentClickTime= SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        mLastClickTime=currentClickTime;

        if(elapsedTime<=MIN_CLICK_INTERVAL)
            return;
        final GraphView graph = (GraphView) mFragmentView.findViewById(R.id.shugarGraph);
        getRange(graph);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.graph_fragment, container, false);
        final GraphView graph = (GraphView) mFragmentView.findViewById(R.id.shugarGraph);
        Button button = mFragmentView.findViewById(R.id.buttonGraphicLevel);

        button.setOnClickListener(this);

        return mFragmentView;
    }

    private void setGraphWithSugarLevelData(GraphView graph, List<SugarDto> listDto) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for(SugarDto dto : listDto){
            series.appendData(new DataPoint(dto.date, dto.level), true, listDto.size());
        }
        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels( listDto.size()-1);
//
////        // set manual x bounds to have nice steps
//        graph.getViewport().setMinX(listDto.get(0).date.getTime());
//        graph.getViewport().setMaxX(listDto.get(listDto.size()-1).date.getTime());
//        graph.getViewport().setXAxisBoundsManual(true);
//
//        graph.getViewport().setMinY(0.0f);
//        graph.getViewport().setMaxY(30.0);
//        graph.getGridLabelRenderer().setNumVerticalLabels(5);
//
//        graph.getGridLabelRenderer().setHumanRounding(false);

    }

    private void getRange(final GraphView graph) {
        mRequestQueue = Volley.newRequestQueue(getContext());
        String url ="http://78.46.233.90:8080/auth/sugar/getrange/1";
        List<FilterState> filters = new ArrayList<>();
        PagingState pagingState = new PagingState();
        SortState sortState = new SortState();
        QueryState query = new QueryState(pagingState, sortState, filters);

//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, query.toJSONArray(),
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        List<SugarDto> listDto = extractSugarDtoList(response);
//                        setGraphWithSugarLevelData(graph, listDto);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                String string = error.toString();
//                showAlertDialog(string);
//            }
//        });
//        jsonArrayRequest.setTag(HTTP_VOLEY_TAG);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, query.toJSONObject(), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                List<SugarDto> listDto = extractSugarDtoList(response);
                setGraphWithSugarLevelData(graph, listDto);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String string = error.toString();
                showAlertDialog(string);

            }
        });
        jsonObjectRequest.setTag(HTTP_VOLEY_TAG);

        mRequestQueue.add(jsonObjectRequest);
    }

    private void showAlertDialog(String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setTitle("Error");
        builder.setMessage(string);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<SugarDto> extractSugarDtoList(JSONArray response) {

        com.bloodsugarlevel.common.Response resp = new com.bloodsugarlevel.common.Response(response);
        try {
            ApiListResult<List<SugarDto>> listResult = ApiListResult.fromResponse(resp, SugarDto.class).getListResult();
            return listResult.getData();
        } catch (Exception e) {
            showAlertDialog(e.getMessage());
        }

        ArrayList<SugarDto> listDto = new ArrayList<>();
//        for (int i = 0; i < response.length() ; i++){
//            try {
//                Object obj = response.get(i);
//                SugarDto dto = null;
//                try {
//                    dto = mapper.readValue(obj.toString(), SugarDto.class);
//                    listDto.add(dto);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }

    private List<SugarDto> extractSugarDtoList(JSONObject response) {

        com.bloodsugarlevel.common.Response resp = new com.bloodsugarlevel.common.Response(response);
        try {
            ApiListResult<List<SugarDto>> listResult = ApiListResult.fromResponse(resp, SugarDto.class).getListResult();
            return listResult.getData();
        } catch (Exception e) {
            showAlertDialog(e.getMessage());
        }

        ArrayList<SugarDto> listDto = new ArrayList<>();
//        for (int i = 0; i < response.length() ; i++){
//            try {
//                Object obj = response.get(i);
//                SugarDto dto = null;
//                try {
//                    dto = mapper.readValue(obj.toString(), SugarDto.class);
//                    listDto.add(dto);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }

    @Override
    public void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(HTTP_VOLEY_TAG);
        }
    }
}