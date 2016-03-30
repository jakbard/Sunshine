package com.example.android.sunshine;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ArrayAdapter<String> mForecastAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        String[] Forecast = {
                "Today - Sunny - 88/63",
                "Tomorrow - Cloudy - 75/54",
                "Saturday - Rainy - 60/45",
                "Sunday - Sunny - 80/79"
        };
        List<String> weekforecast = new ArrayList<String>(Arrays.asList(Forecast));
        mForecastAdapter = new ArrayAdapter<String>(
        getActivity(),
        R.layout.listitemforecast,
        R.id.list_item_forecast_textview,
        weekforecast);
        ListView myListView = (ListView) rootview.findViewById(R.id.listview_forecast);
        myListView.setAdapter(mForecastAdapter);
        return rootview;
    }
}