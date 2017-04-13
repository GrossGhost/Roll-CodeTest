package com.example.gross.rollcodetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import static com.example.gross.rollcodetest.Constants.BROADCAST_ACTION;
import static com.example.gross.rollcodetest.Constants.COUNT;
import static com.example.gross.rollcodetest.Constants.LAST_DATA;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvTime, tvCount;
    Button btnStart, btnStop;
    String count;
    SharedPreferences sPref;
    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTime = (TextView) findViewById(R.id.tVTime);
        tvCount = (TextView) findViewById(R.id.tVCount);


        //считывание данных с SharedPrefences
        sPref = getSharedPreferences("", MODE_PRIVATE);
        tvTime.setText(sPref.getString(LAST_DATA, "Last Data"));
        tvCount.setText(String.valueOf(sPref.getLong(COUNT, 0)));

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tvTime.setText(intent.getStringExtra(LAST_DATA));
                count = String.valueOf(intent.getLongExtra(COUNT, 0));
                tvCount.setText(count);

            }
        };
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intentFilter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnStart:
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startService(new Intent(this, MyService.class));
                btnStart.setClickable(false);
                btnStop.setClickable(true);
                break;

            case R.id.btnStop:
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopService(new Intent(this, MyService.class));
                btnStart.setClickable(true);
                btnStop.setClickable(false);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, MyService.class));
        unregisterReceiver(br);
        super.onDestroy();
    }
}
