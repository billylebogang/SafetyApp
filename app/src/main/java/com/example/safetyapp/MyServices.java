package com.example.safetyapp;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Provider;

public class MyServices extends Service {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    @Nullable
    FirebaseUser USER;
    DatabaseReference ref;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        USER = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("hailing");

        Coodinate coodinate = (Coodinate)intent.getSerializableExtra("coodinate");

        new Thread(new Runnable() {

            @Override
            public void run() {

                while(true){
                    Log.e( "Service","run: I AM A SERVICE AND I AM RUNNING AT: "+ coodinate.getCountry());
                    try {

                        Thread.sleep(60000);

                        sendSms(coodinate);

                        ref.child(USER.getUid()).child(System.currentTimeMillis()+"").setValue(coodinate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    Toast.makeText(MyServices.this, "Sent location", Toast.LENGTH_SHORT).show();

                                    vibe();
                                }
                            }
                        });

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


    
    public void vibe(){
        final VibrationEffect vibrationEffect1;

        final Vibrator vibrator = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        
        // this is the only type of the vibration which requires system version Oreo (API 26)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
            vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);

            // it is safe to cancel other vibrations currently taking place
            vibrator.cancel();
            
            vibrator.vibrate(vibrationEffect1);
        }
    }

    public  void sendSms(Coodinate coor){


       /* if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG", getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(MyServices.this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted. Enable the SMS button.*/

        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        try {

            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage("+26775525404", null, "i am attacked at"+ coor.toString(), pi, null);

            Log.e("SEND SMS", "sendSms: message sent" );
               // Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            Log.e("SEND SMS", "sendSms: "+e.getMessage() );
                //Toast.makeText(getApplicationContext(), "Some fields is Empty", Toast.LENGTH_LONG).show();
        }

    }

}
