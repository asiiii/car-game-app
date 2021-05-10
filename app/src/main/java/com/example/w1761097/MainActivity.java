package com.example.w1761097;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    Button timer;
    boolean timerStatus;
    //timer status holds the status to the ToggleButton timer
    //It is passed as an Extra for SecondActivity, ThirdActivity, FourthActivity and Fifth Activity
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = findViewById(R.id.button_timer);
    }

    //method to launch the SecondActivity for the Identify the Car Make game
    public void launchIdentifyCarMake(View view) {
        Log.d(LOG_TAG, "Identify Car Make Button clicked!");
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("Timer status", timerStatus);
        startActivity(intent);
    }

    //method to launch the ThirdActivity for the Hints game
    public void launchHints(View view) {
        Log.d(LOG_TAG, "Hints Button clicked!");
        Intent intent = new Intent(this, ThirdActivity.class);
        intent.putExtra("Timer status", timerStatus);
        startActivity(intent);
    }

    //method to launch the FourthActivity for the Identify Car Image game
    public void launchIdentifyCarImage(View view) {
        Log.d(LOG_TAG, "Identify Car Image Button clicked!");
        Intent intent = new Intent(this, FourthActivity.class);
        intent.putExtra("Timer status", timerStatus);
        startActivity(intent);
    }

    //method to launch the FifthActivity for the Advanced Level
    public void launchAdvancedLevel(View view) {
        Log.d(LOG_TAG, "Advanced Level Button clicked!");
        Intent intent = new Intent(this, FifthActivity.class);
        intent.putExtra("Timer status", timerStatus);
        startActivity(intent);
    }

    //method to set the value of the ToggleButton timer
    public void onTimerButtonClick(View view) {
        String text = timer.getText().toString();
        Log.d(LOG_TAG, "Timer status: " + timerStatus);
        if(text.equals(getString(R.string.timer_off_text))){
            timerStatus = true;
            timer.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_timer_on, null, null, null);
            timer.setText(getString(R.string.timer_on_text));
        }else {
            timerStatus= false;
            timer.setButtonDrawable(R.drawable.ic_baseline_timer_off);
            timer.setText(getString(R.string.timer_off_text));

        }
    }
}