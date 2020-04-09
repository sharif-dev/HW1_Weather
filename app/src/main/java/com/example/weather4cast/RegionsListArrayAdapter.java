package com.example.weather4cast;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weather4cast.networking.ConnectionChecker;

import java.util.ArrayList;

public class RegionsListArrayAdapter extends BaseAdapter {
    private final Activity context;
    private final ArrayList<RegionData> regionDataArrayList;

    RegionsListArrayAdapter(Activity context, ArrayList<RegionData> regionDataArrayList) {
        this.context = context;
        this.regionDataArrayList = regionDataArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return regionDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return regionDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.regions_list_row, null, true);

            holder.regionName = convertView.findViewById(R.id.regionName);
            holder.regionDetails = convertView.findViewById(R.id.regionDetails);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.regionName.setText(regionDataArrayList.get(position).name);
        holder.regionDetails.setText(regionDataArrayList.get(position).details);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Details", regionDataArrayList.get(position).name +
                        " " + regionDataArrayList.get(position).latitude +
                        " " + regionDataArrayList.get(position).longitude);
                ForecastDisplay forecastDisplay;
                if (ConnectionChecker.RVConnected(context)) {
                    forecastDisplay = ForecastDisplay.newOnlineInstance
                            (regionDataArrayList.get(position).latitude,
                                    regionDataArrayList.get(position).longitude);
                }else {
                    forecastDisplay = ForecastDisplay.newOfflineInstance();
                }
                context.getFragmentManager().beginTransaction().add(R.id.search_activity, forecastDisplay).commit();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView regionName, regionDetails;
    }
}
