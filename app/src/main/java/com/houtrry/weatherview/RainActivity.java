package com.houtrry.weatherview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.houtrry.weatherviewlibrary.FlakeView;

public class RainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rain);

        final FlakeView rainView = findViewById(R.id.rain_flake_view);
        rainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainView.setWeatherType(rainView.getWeatherType() == FlakeView.TYPE_RAIN? FlakeView.TYPE_SNOW:FlakeView.TYPE_RAIN);
                rainView.refresh();
            }
        });
    }
}
