package com.example.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout homeRl;
    private ProgressBar loadingPb;
    private TextView cityNameTv, temperatureTv, conditionTv;
    private TextInputEditText cityEdt;
    private ImageView IconIv, backIv, searchIv;
    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView weatherRv;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private final int PERMISSION_CODE = 1;
    private String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_main);
        homeRl = findViewById(R.id.IDRLHOME);
        loadingPb = findViewById(R.id.PBLoading);
        cityNameTv = findViewById(R.id.idTVCityName);
        temperatureTv = findViewById(R.id.idTVTemperature);
        conditionTv = findViewById(R.id.IdTvConditions);
        cityEdt = findViewById(R.id.idEdtcity);
        IconIv = findViewById(R.id.IdIVIcon);
        backIv = findViewById(R.id.IVBack);
        searchIv = findViewById(R.id.idIVSRCH);
        weatherRv = findViewById(R.id.idRVWeather);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);
        weatherRv.setAdapter(weatherRVAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            cityName = getCityName(location.getLongitude(), location.getLatitude());
                            getWeatherInfo(cityName);
                        }
                    }
                });

        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEdt.getText().toString();
                if (city.isEmpty() || city.equals("")) {
                    Toast.makeText(MainActivity.this, "enter city name", Toast.LENGTH_SHORT).show();
                } else {
                    cityNameTv.setText(cityName);
                    getWeatherInfo(city);

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }

    private String getCityName(double longitude, double latitude) {
        String cityname = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            for (Address adr : addresses) {
                if (adr != null) {
                    String city = adr.getLocality();
                    if (city != null && !city.equals("")) {
                        cityname = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "User City Not Found...", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityname;
    }

    private void getWeatherInfo(String cityName) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=f0e0c88eb45d40adb41180658211610&q=" + cityName + "&days=1&aqi=yes&alerts=yes";
        cityNameTv.setText(cityName);
        RequestQueue queue = Volley.newRequestQueue(this);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPb.setVisibility(View.GONE);
                homeRl.setVisibility(View.VISIBLE);
                weatherRVModalArrayList.clear();
                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTv.setText(temperature + "°c");
                    int isDay = response.optJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(IconIv);
                    conditionTv.setText(condition);
                    if (isDay == 1) {
                        Picasso.get().load("https://media.istockphoto.com/photos/panorama-blue-sky-and-clouds-with-daylight-natural-background-picture-id1263264401?b=1&k=20&m=1263264401&s=170667a&w=0&h=MGYX3CWnqqkb82ps4jeyIczq5_zl2hPt3s9wUdBYhpQ=").into(backIv);
                    } else {
                        Picasso.get().load("https://media.istockphoto.com/photos/sky-full-of-stars-picture-id512250305?b=1&k=20&m=512250305&s=170667a&w=0&h=x_Y7xuAMU6llJlKGm7dXDZsCitof4mYGH0YmUasFha8=").into(backIv);
                    }
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastO = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastO.getJSONArray("hour");
                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        WeatherRVModal modal = new WeatherRVModal(time, temper, img, wind);
                        weatherRVModalArrayList.add(new WeatherRVModal(time, temper, img, wind));
                        Log.d("vivz", "data updated");
                    }
                    Log.d("vivz", "data updated called");
                    weatherRVAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "please enter valid city name", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

}


