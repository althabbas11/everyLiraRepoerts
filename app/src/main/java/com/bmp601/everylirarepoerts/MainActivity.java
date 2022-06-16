package com.bmp601.everylirarepoerts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition(() -> true);

        Intent intent = new Intent(getApplicationContext(), ReportsActivity.class);
        startActivity(intent);
        finish();
    }
}