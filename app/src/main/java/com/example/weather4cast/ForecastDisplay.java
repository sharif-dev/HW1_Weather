package com.example.weather4cast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.weather4cast.adapters.DaysRecyclerViewAdapter;
import com.example.weather4cast.model.WeatherResponse;
import com.example.weather4cast.networking.WeatherAPI;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ForecastDisplay extends Fragment {
    private static final String LAT = "lat";
    private static final String LON = "lon";

    private Handler handler = new Handler();
    private WeatherResponse ws;
    private DaysRecyclerViewAdapter drva;

    public static ForecastDisplay newOnlineInstance(String lat, String lon) {
        ForecastDisplay fragment = new ForecastDisplay();

        Bundle args = new Bundle();
        args.putString(LAT, lat);
        args.putString(LON, lon);
        fragment.setArguments(args);

        return fragment;
    }

    public static ForecastDisplay newOfflineInstance() {
        return new ForecastDisplay();
    }

    public ForecastDisplay() {
        // Required empty public constructor
    }

    private void getWsFromAPI(String lat, String lon) {
        WeatherAPI.getWeatherFromApi(lat, lon, new WeatherAPI.ICallback() {
            @Override
            public void onWeatherResponse(WeatherResponse ws) {
                FileManager.StoreWeatherResponse(ws, getActivity());
                handler.post(() -> {
                    drva.setWeatherResponse(ws);
                    progressBar.setVisibility(View.GONE);
                    cityNameTextView.setText(ws.cityNameC.cityName);
                    recyclerView.setVisibility(View.VISIBLE);
                    cityNameTextView.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onWeatherError(VolleyError error) {
                Log.e("tag", error.toString());
                handler.post(() -> {
                    Toast.makeText(getActivity(), "Couldn't connect to server", Toast.LENGTH_LONG).show();
                });
                getWsFromFile();
            }
        });
    }

    private void getWsFromFile() {
        FileManager.getWeatherResponse(getActivity(), new FileManager.IGetWeatherResponseCB() {
            @Override
            public void onSuccess(WeatherResponse ws) {
                handler.post(() -> {
                    drva.setWeatherResponse(ws);
                    progressBar.setVisibility(View.GONE);
                    cityNameTextView.setText(ws.cityNameC.cityName);
                    recyclerView.setVisibility(View.VISIBLE);
                    cityNameTextView.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onError() {
                handler.post(() -> {
                    Toast.makeText(getActivity(), "No offline data was found", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }

    @BindView(R.id.forcast_frag_pb)
    ProgressBar progressBar;
    @BindView(R.id.forecast_page_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.forecast_city_name)
    TextView cityNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragLayout = inflater.inflate(R.layout.fragment_forecast_display, container, false);
        ButterKnife.bind(this, fragLayout);
        recyclerView.setVisibility(View.GONE);
        cityNameTextView.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        drva = new DaysRecyclerViewAdapter(ws);
        recyclerView.setAdapter(drva);

        //getting data
        if (getArguments() != null) {
            String lat = getArguments().getString(LAT);
            String lon = getArguments().getString(LON);

            getWsFromAPI(lat, lon);
        } else {
            getWsFromFile();
        }

        return fragLayout;
    }
}
