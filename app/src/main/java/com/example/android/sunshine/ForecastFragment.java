package com.example.android.sunshine;
import android.net.Uri;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    private ArrayAdapter<String> mForecastAdapter;

    public ForecastFragment() {
           }
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
   }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.forecastfragment, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    switch (item.getItemId()) {
        case R.id.action_refresh:
            FetchWeatherTask Fetch = new FetchWeatherTask();
            Fetch.execute("94043");
        default:
            return super.onOptionsItemSelected(item);
                }
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

    public class FetchWeatherTask extends AsyncTask <String, Void, Void > {
            private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
            @Override
            protected Void doInBackground(String... params){
                if (params.length  ==0) {
                    return null;
                }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
             //  URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID="+BuildConfig.OPEN_WEATHER_MAP_API_KEY.toString());

                // Create the request to OpenWeatherMap, and open the connection
                final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String query_param = "q";
                final String format_param = "mode";
                final String units_param = "units";
                final String count_param = "cnt";
                final int numdays =7;
                final String appid_param = "APPID";

                Uri fecthweather = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(query_param, params[0])
                        .appendQueryParameter(format_param, "json")
                        .appendQueryParameter(units_param, "metrics")
                        .appendQueryParameter(count_param, Integer.toString(numdays))
                        .appendQueryParameter(appid_param,BuildConfig.OPEN_WEATHER_MAP_API_KEY.toString())
                        .build();

                URL url = new URL(fecthweather.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Forecast JSON String:" + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        return null;
            }
    }
}
