package com.example.weather4cast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather4cast.networking.ConnectionChecker;
import com.example.weather4cast.networking.VSReqQ;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private final String mapboxSearchURL = "https://api.mapbox.com/geocoding/v5/mapbox.places/{query}.json?types={type}&access_token={your_token}";
    private final String mapboxRequestType = "region";
    private final String mapboxToken = "pk.eyJ1IjoiYWxpajEiLCJhIjoiY2s4cjVjOG9vMDN5bTNrbG8xaHo4endkbyJ9.fl5D9Sne8JUjEqA_mN3geQ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Picasso.get().setLoggingEnabled(true);
        VSReqQ.initialize(getApplicationContext());
        initializeSearchActivity();
    }

    private void initializeSearchActivity() {
        setContentView(R.layout.activity_search);

        final EditText searchFilled = findViewById(R.id.searchFilled);
        final ListView regionsList = findViewById(R.id.regionsList);
        final ProgressBar progressBar = findViewById(R.id.search_region_pb);

        regionsList.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        searchFilled.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchForRegionsWithNewThread(searchFilled.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    private void searchForRegionsWithNewThread(String query) {
        final ListView regionsList = findViewById(R.id.regionsList);
        final ProgressBar progressBar = findViewById(R.id.search_region_pb);
        final TextView searchResult = findViewById(R.id.searchResultTextView);

        regionsList.setVisibility(View.INVISIBLE);
        searchResult.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final RequestQueue queue = Volley.newRequestQueue(this);

        String url = mapboxSearchURL.replace("{query}", query)
                .replace("{type}", mapboxRequestType)
                .replace("{your_token}", mapboxToken);

        final AppCompatActivity thisActivity = this;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<RegionData> regionDataList = RegionData.getDataArray(response);
                        if (regionDataList.size() > 0) {
                            RegionsListArrayAdapter regionsListArrayAdapter = new RegionsListArrayAdapter
                                    (thisActivity, regionDataList);
                            regionsList.setAdapter(regionsListArrayAdapter);

                            progressBar.setVisibility(View.INVISIBLE);
                            regionsList.setVisibility(View.VISIBLE);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            searchResult.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", "mapbox request failed");
            }
        });

        Thread searchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                queue.add(stringRequest);
            }
        });

        if (ConnectionChecker.RVConnected(this)) {
            searchThread.start();
        } else {
            Toast.makeText(this, "Couldn't connect to server", Toast.LENGTH_LONG).show();
            ForecastDisplay forecastDisplay = ForecastDisplay.newOfflineInstance();
            this.getSupportFragmentManager().beginTransaction().add(R.id.search_activity, forecastDisplay)
                    .addToBackStack("forecast fragment").commit();
        }
    }
}
