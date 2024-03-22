package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textview;
    MaterialButton reset, start, stop;
    int seconds, minutes, milliSeconds;
    long milliSecond, startTime, timeBuff, updateTime = 0L;
    Handler handler;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            milliSecond = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + milliSecond;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 1000);

            textview.setText(MessageFormat.format("{0}:{1}:{2}", minutes, String.format(Locale.getDefault(), "%02d", seconds), String.format(Locale.getDefault(), "%02d", milliSeconds)));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview = findViewById(R.id.textView);
        reset = findViewById(R.id.reset);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);

        handler = new Handler(Looper.getMainLooper());

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                reset.setEnabled(false);
                start.setEnabled(false);
                stop.setEnabled(true);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += milliSecond;
                handler.removeCallbacks(runnable);
                reset.setEnabled(true);
                start.setEnabled(true);
                stop.setEnabled(false);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                milliSecond = 0L;
                startTime = 0L;
                timeBuff = 0L;
                updateTime = 0L;
                seconds = 0;
                minutes = 0;
                milliSeconds = 0;
                textview.setText("0:00:00");
            }
        });
        textview.setText("00:00:00");
    }
}