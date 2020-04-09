package com.example.weather4cast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    private final String mapboxSearchURL = "https://api.mapbox.com/geocoding/v5/mapbox.places/{query}.json?types={type}&access_token={your_token}";
    private final String mapboxRequestType = "region";
    private final String mapboxToken = "pk.eyJ1IjoiYWxpajEiLCJhIjoiY2s4cjVjOG9vMDN5bTNrbG8xaHo4endkbyJ9.fl5D9Sne8JUjEqA_mN3geQ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeSearchActivity();
    }

    private void initializeSearchActivity() {
        setContentView(R.layout.activity_search);

        final EditText searchFilled = findViewById(R.id.searchFilled);
        ListView regionsList = findViewById(R.id.regionsList);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView searchResult = findViewById(R.id.searchResultTextView);

        regionsList.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        searchResult.setVisibility(View.INVISIBLE);


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
        final RequestQueue queue = Volley.newRequestQueue(this);

        String url = mapboxSearchURL.replace("{query}", query)
                .replace("{type}", mapboxRequestType)
                .replace("{your_token}", mapboxToken);

        final Activity thisActivity = this;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            RegionsListArrayAdapter regionsListArrayAdapter =
                                    RegionsListArrayAdapter.getNewRegionsListArrayAdapter
                                            (thisActivity, (JSONArray) obj.get("features"));
                            ListView regionsList = findViewById(R.id.regionsList);
                            regionsList.setAdapter(regionsListArrayAdapter);
                        } catch (JSONException e) {
                            Log.e("JsonParseError", "wrong json response from mapbox server");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RequestFailed", "mapbox request failed");
            }
        });

        Thread searchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                queue.add(stringRequest);
            }
        });
        searchThread.run();
    }
}
