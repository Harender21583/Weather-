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
    private ArrayList<WeatherRVModal>weatherRVModalArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalArrayList) {
        this.context = context;
        this.weatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
        WeatherRVModal modal=weatherRVModalArrayList.get(position);
        holder.tempraturetv.setText(modal.getTamprature()+"°c");
        holder.windtv.setText(modal.getWindspeed()+"km/h");
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.coditioniv);
        SimpleDateFormat input=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");
        try {
            Date t=input.parse(modal.getTime());
            holder.timetv.setText(output.format(t));
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView windtv,timetv,tempraturetv;
        private ImageView coditioniv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windtv=itemView.findViewById(R.id.idTVWindspeed);
            timetv=itemView.findViewById(R.id.idTVTime);
            tempraturetv=itemView.findViewById(R.id.IDTVTemprature);
            coditioniv=itemView.findViewById(R.id.IdTVCondition);
        }
    }
}
