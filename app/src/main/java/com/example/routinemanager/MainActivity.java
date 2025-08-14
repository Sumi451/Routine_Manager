package com.example.routinemanager;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Use a Handler to wait for 2 seconds before moving to the home screen
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to start the HomeScreenActivity
                Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                startActivity(intent);

                // Finish the current activity to prevent going back to the splash screen
                finish();
            }
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}
