package com.example.weather4cast.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather4cast.R;
import com.example.weather4cast.model.Day;
import com.example.weather4cast.model.WeatherResponse;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaysRecyclerViewAdapter extends RecyclerView.Adapter<DaysRecyclerViewAdapter.DayViewHolder> {
    private WeatherResponse ws;

    public DaysRecyclerViewAdapter(WeatherResponse ws) {
        this.ws = ws;
    }

    public void setWeatherResponse(WeatherResponse ws) {
        this.ws = ws;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_card, parent, false);
        return new DayViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day day = ws.daysC.days.get(position);

        Picasso.get().load(day.weatherCondition.verdictIconUrl).into(holder.verdictIcon);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE");
        String dayOfWeek;
        try {
            dayOfWeek = format2.format(format1.parse(day.date));
        } catch (Exception e) {
            e.printStackTrace();
            dayOfWeek = day.date;
        }
        holder.dayOfWeek.setText(dayOfWeek);

        holder.verdictText.setText(day.weatherCondition.weatherVerdict);
    }

    @Override
    public int getItemCount() {
        return ws != null ? ws.daysC.days.size() : 0;
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day_of_week)
        TextView dayOfWeek;

        @BindView(R.id.verdict_icon)
        ImageView verdictIcon;

        @BindView(R.id.verdict_text)
        TextView verdictText;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
