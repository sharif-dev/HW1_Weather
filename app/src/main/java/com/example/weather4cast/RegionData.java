package com.example.weather4cast;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegionData {
    public String name, details, latitude, longitude;

    public static ArrayList<RegionData> getDataArray(String response) {
        ArrayList<RegionData> dataArray = new ArrayList<>();
        try {
            JSONArray data = (new JSONObject(response)).getJSONArray("features");
            for (int i = 0; i < data.length(); i++) {
                dataArray.add(new RegionData(data.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.e("tag", "wrong json response from mapbox server");
        }
        return dataArray;
    }

    private RegionData(JSONObject object) {
        try {
            name = object.getString("text");
            details = object.getString("place_name");
            JSONArray coordinates =
                    object.getJSONObject("geometry").getJSONArray("coordinates");
            latitude = coordinates.get(1).toString();
            longitude = coordinates.get(0).toString();
        } catch (JSONException e) {
            Log.e("tag", "wrong json response from mapbox server");
        }
    }
}
