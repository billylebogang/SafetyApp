package com.example.safetyapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.security.Provider;

public class MyServices extends Service {
    @Nullable





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Coodinate coodinate = (Coodinate)intent.getSerializableExtra("coodinate");

        new Thread(new Runnable() {


            @Override
            public void run() {

                while(true){


                    Log.e( "Service","run: I AM A SERVICE AND I AM RUNNING AT: "+ coodinate.getCountry());


                    try {

                        Thread.sleep(2000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




}
