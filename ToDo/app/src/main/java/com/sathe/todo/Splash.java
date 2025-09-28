package com.sathe.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    private TextView splashText;
    private String massage="Let's get your life organized !";
    private int index=0;
    private long delay=150;//delay in ms per charecter

    private Handler handler=new Handler(Looper.getMainLooper());

    private Runnable charecterAdder=new Runnable() {
        @Override
        public void run() {
            splashText.setText(massage.substring(0, index++));
            if (index<=massage.length()) {

//
                handler.postDelayed(charecterAdder, delay);
            } else {
                handler.postDelayed(() -> {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        splashText = findViewById(R.id.splashText);

        handler.postDelayed(charecterAdder,delay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(charecterAdder);
    }
}