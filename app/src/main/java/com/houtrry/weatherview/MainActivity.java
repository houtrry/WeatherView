package com.houtrry.weatherview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * @author houtrry
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_snow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAty(SnowActivity.class);
            }
        });

        findViewById(R.id.tv_rain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAty(RainActivity.class);
            }
        });
    }

    private void toAty(Class<? extends Activity> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }
}
