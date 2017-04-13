package com.example.gross.rollcodetest;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;

import static com.example.gross.rollcodetest.Constants.BROADCAST_ACTION;
import static com.example.gross.rollcodetest.Constants.COUNT;
import static com.example.gross.rollcodetest.Constants.LAST_DATA;
import static java.lang.Thread.sleep;


public class MyService extends Service {

    long data, count;
    Intent i = new Intent(BROADCAST_ACTION);
    Thread t;
    boolean isActive = true;
    SharedPreferences sPref;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startThread();
        data = System.currentTimeMillis();

        //записываем время в SharedPref
        sPref = getSharedPreferences("", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(LAST_DATA, sdf.format(data));
        ed.commit();

        //отправляем время в Активити
        i.putExtra(LAST_DATA, sdf.format(data));
        sendBroadcast(i);

        return super.onStartCommand(intent, flags, startId);
    }

    private void startThread() {

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                sPref = getSharedPreferences("", MODE_PRIVATE);
                count = sPref.getLong(COUNT, 0);
                while (isActive) {
                    count++;
                    i.putExtra(COUNT, count);
                    sendBroadcast(i);
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //записываем count в SharedPref
                SharedPreferences.Editor ed = sPref.edit();
                ed.putLong(COUNT, count);
                ed.commit();
            }
        });
        t.start();
    }

    @Override
    public void onDestroy() {
        isActive = false;
        super.onDestroy();
    }
}
