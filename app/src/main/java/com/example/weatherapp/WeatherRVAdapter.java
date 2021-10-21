package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalArrayList) {
        this.context = context;
        this.weatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
        WeatherRVModal modal = weatherRVModalArrayList.get(position);
        holder.temperatureTv.setText(modal.getTamprature() + "°c");
        holder.windTv.setText(modal.getWindspeed() + "km/h");
        String val = "http:".concat(modal.getIcon());
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.conditionIv);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date t = input.parse(modal.getTime());
            holder.timeTv.setText(output.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView windTv, timeTv, temperatureTv;

        private ImageView conditionIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTv = itemView.findViewById(R.id.idTvWindSpeed);
            timeTv = itemView.findViewById(R.id.idTVTime);
            temperatureTv = itemView.findViewById(R.id.IdTvTemperature);
            conditionIv = itemView.findViewById(R.id.IdTvCondition);
        }
    }
}
