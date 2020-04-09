package com.example.weather4cast;

import android.app.Activity;
import android.widget.ArrayAdapter;

import org.json.JSONObject;

public class RegionsListArrayAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] regionArray;
    private final String[] detailArray;
    private final float[] latitudeArray;
    private final float[] longitudeArray;

    public RegionsListArrayAdapter(Activity context, JSONObject[] data) {
        super(context, R.layout);
        this.context = context;
        regionArray = new String[data.length];
        detailArray = new String[data.length];
        latitudeArray = new float[data.length];
        longitudeArray = new float[data.length];
    }
}
