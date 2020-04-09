package com.example.weather4cast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weather4cast.adapters.DaysRecyclerViewAdapter;
import com.example.weather4cast.model.WeatherResponse;
import com.example.weather4cast.networking.WeatherAPI;


public class ForecastDisplay extends Fragment {
    private static final String CITY_NAME = "cityName";

    private WeatherResponse ws;

    public ForecastDisplay() {
        // Required empty public constructor
    }

    public static ForecastDisplay newInstance(String cityName) {
        ForecastDisplay fragment = new ForecastDisplay();
        Bundle args = new Bundle();
        args.putString(CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String cityName = getArguments().getString(CITY_NAME);
            if(cityName==null){
                setWsFromFile();
            }else {
                setWsFromAPI(cityName);
            }
        }
    }

    private void setWsFromAPI(String cityName) {
        ws = WeatherAPI.getWeatherFromApi(cityName);
        if(ws == null){
            Toast.makeText(requireContext(),"Couldn't connect to server",Toast.LENGTH_LONG).show();
            setWsFromFile();
        }
    }

    private void setWsFromFile() {
        ws = FileManager.getWeatherResponse();
        if (ws == null) {
            Toast.makeText(requireContext(), "No offline data was found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragLayout = inflater.inflate(R.layout.fragment_forecast_display, container, false);
        RecyclerView recyclerView = fragLayout.findViewById(R.id.forecast_page_recycler_view);
        DaysRecyclerViewAdapter adapter = new DaysRecyclerViewAdapter(ws);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return fragLayout;
    }
}
