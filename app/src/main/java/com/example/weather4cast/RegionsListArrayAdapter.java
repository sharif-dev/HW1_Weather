package com.example.weather4cast;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegionsListArrayAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] regionArray;
    private final String[] detailArray;
    private final double[] latitudeArray;
    private final double[] longitudeArray;

    static RegionsListArrayAdapter getNewRegionsListArrayAdapter(Activity context, JSONArray data) {
        String[] regionArray = new String[data.length()];
        String[] detailArray = new String[data.length()];
        double[] latitudeArray = new double[data.length()];
        double[] longitudeArray = new double[data.length()];
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject element = (JSONObject) data.get(i);
                regionArray[i] = (String) element.get("text");
                detailArray[i] = (String) element.get("place_name");
                JSONArray coordinates = (JSONArray)
                        ((JSONObject) element.get("geometry")).get("coordinates");
                Log.i("aaaa", regionArray[i] + " " + coordinates.get(1) +" "+ coordinates.get(0));
                try {
                    latitudeArray[i] = (double) coordinates.get(1);
                } catch (Exception e) {
                    latitudeArray[i] = 0;
                    latitudeArray[i] += (int) coordinates.get(1);
                }
                try {
                    longitudeArray[i] = (double) coordinates.get(0);
                } catch (Exception e) {
                    longitudeArray[i] = 0;
                    longitudeArray[i] += (int) coordinates.get(0);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new RegionsListArrayAdapter
                (context, regionArray, detailArray, latitudeArray, longitudeArray);
    }

    private RegionsListArrayAdapter(Activity context, String[] regionArray, String[] detailArray,
             double[] latitudeArray, double[] longitudeArray) {
        super(context, R.layout.regions_list_row, regionArray);
        this.context = context;
        this.regionArray = regionArray;
        this.detailArray = detailArray;
        this.latitudeArray = latitudeArray;
        this.longitudeArray = longitudeArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater=context.getLayoutInflater();
//        View rowView=inflater.inflate(R.layout.regions_list_row, null,true);
//
//        TextView regionName = rowView.findViewById(R.id.regionName);
//        TextView regionDetails = rowView.findViewById(R.id.regionDetails);
//
//        regionName.setText(regionArray[position]);
//        regionDetails.setText(detailArray[position]);
//
//        return rowView;

        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.regions_list_row, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.regionName)).setText(regionArray[position]);
        ((TextView) convertView.findViewById(R.id.regionDetails)).setText(detailArray[position]);
        return convertView;
    }
}
