package com.example.safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements SensorListener {

    //for testing if the thing works
    TextView results,profilename,profilelocation,profilenumber;

    Button activate, signoutBtn;
    //button to activate if the app can listen to background activity

    Coodinate coodinate;

    //database stuff
    FirebaseAuth auth;
    FirebaseUser USER;
    FirebaseDatabase database;
    DatabaseReference myRef;

    //usefull user objects
    User user;
    Profile profile;


    boolean ACTIVATOR = false;
    FusedLocationProviderClient fusedLocationProviderClient;


    //sensor

    private SensorManager sensorMgr;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        results = findViewById(R.id.results);
        activate = findViewById(R.id.activateBtn);
        signoutBtn = findViewById(R.id.signoutBtn);
        profilename = findViewById(R.id.profilename);
        profilelocation = findViewById(R.id.profilelocation);
        profilenumber = findViewById(R.id.profilenumber);


        coodinate = new Coodinate();

        User user = new User();

        user.toString();


        //database stuff
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User_profiles");
        auth = FirebaseAuth.getInstance();
        USER = auth.getCurrentUser();

        //shaking sensor

        // start motion detection
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        boolean accelSupported = sensorMgr.registerListener(this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);

        if (!accelSupported) {
            // on accelerometer on this device
            sensorMgr.unregisterListener(this,
                    SensorManager.SENSOR_ACCELEROMETER);
        }


        //getting user details to display
        getData();


        //location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (!ACTIVATOR) {
            results.setText("Service not active");
        }

        //set on click to get location
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ACTIVATOR = true;
                //checking for permission
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when permission ins given
                    getLocation();

                } else {

                    //requesting permission-- cus it is not given
                    ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }


            }
        });




        //sign out
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });
    }


    //method to get Location
    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {


                if (task.isSuccessful()) {

                    Location location = task.getResult();

                    if (location != null) {

                        try {

                            //initialising geocoder
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                            //list for address
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            //instantiating the coordinate object
                            coodinate.setCountry(addresses.get(0).getCountryName());
                            coodinate.setLatitudes(String.valueOf(addresses.get(0).getLatitude()));
                            coodinate.setLongitudes(String.valueOf(addresses.get(0).getLongitude()));
                            coodinate.setTown(addresses.get(0).getLocality());

                            Toast.makeText(MainActivity.this, "Location fetched at: "+addresses.get(0).getCountryName(), Toast.LENGTH_SHORT).show();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    results.setText("" + task.getException().getMessage());
                    Toast.makeText(MainActivity.this, "It has failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //method to detect button press

    int a  = 0,b=a,a1 = 0,b1 = a1;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        int action, keycode, countU = 0, countD = 0;
        action = event.getAction();
        keycode= event.getKeyCode();

        //when a key is pushed down

        if(KeyEvent.ACTION_DOWN == action && ACTIVATOR){

            if(keycode == KeyEvent.KEYCODE_VOLUME_DOWN){
               //VOLUME DOWN KEY

                a++; //keeping track of how long it takes to keep it down

                results.setText("Volume down pressed down");

            }
            else if(keycode == KeyEvent.KEYCODE_VOLUME_UP){
                //VOLUME UP KEY
                a1++; //keeping track of long int takes to keep down

                results.setText("Volume up pressed down");
            }
            else{
                throw new IllegalStateException("Unexpected value: " + keycode);
            }

        }
        else{
            Toast.makeText(this, "Please activate", Toast.LENGTH_SHORT).show();
            
        }

     //when a key is realised up

        if(KeyEvent.ACTION_UP == action && ACTIVATOR){

            switch (keycode){
                case KeyEvent.KEYCODE_VOLUME_DOWN: {

                    if(a > b+80 ){


                        results.setText("Volume down pressed up action is ON");

                        Intent serviceIntent = new Intent(MainActivity.this, MyServices.class);

                        serviceIntent.putExtra("coodinate", coodinate);

                        startService(serviceIntent);

                    }
                    else{
                        results.setText("Volume down pressed fast");
                    }


                    break;
                }
                case KeyEvent.KEYCODE_VOLUME_UP: {

                    if(a1 > b1+80 ){

                        results.setText("Volume up pressed up action in ON");

                        Intent serviceIntent = new Intent(MainActivity.this, MyServices.class);

                        serviceIntent.putExtra("coodinate", coodinate);

                        startService(serviceIntent);
                    }
                    else{
                        results.setText("Volume up pressed fast");
                    }

                    break;
                }

                default:
                    throw new IllegalStateException("Unexpected value: " + keycode);
            }

            //very very important
            b=a;
            b1=a1;

        }
        else{
            Toast.makeText(this, "Please Activate", Toast.LENGTH_SHORT).show();
        }

        return super.dispatchKeyEvent(event);
    }


    //method to get data from db

    public void getData(){

       myRef.child(USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {


                   //casting results into a profile object
                   profile = snapshot.getValue(Profile.class);

                   Log.e("GET VALUE", "onComplete: "+ snapshot.getValue() );
                   Log.e("GET RESULTS", "onComplete: "+ snapshot );

                   if(profile !=null){

                       profilename.setText(profile.getName()+" "+profile.getSurname());
                       profilelocation.setText(profile.getAddress());
                       profilenumber.setText(profile.getNumber());
                   }
                   else{
                       Toast.makeText(MainActivity.this, "Failed: Profile is null ", Toast.LENGTH_SHORT).show();
                       Log.e("MAIN ACTIVITY", "onComplete: PROFILE IS NULL" );
                       startActivity(new Intent(MainActivity.this, Login.class));
                       finish();
                   }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

               Toast.makeText(MainActivity.this, "Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();

               startActivity(new Intent(MainActivity.this, Login.class));

               finish();
           }
       });

    }

    @Override
    public void onAccuracyChanged(int i, int i1) {

    }


    protected void onPause() {
        if (sensorMgr != null) {
            sensorMgr.unregisterListener(this,
                    SensorManager.SENSOR_ACCELEROMETER);
            sensorMgr = null;
        }
        super.onPause();
    }



    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();

            // only allow one update every 1000ms.
            if ((curTime - lastUpdate) > 700) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = values[SensorManager.DATA_X];
                y = values[SensorManager.DATA_Y];
                z = values[SensorManager.DATA_Z];

                if(Round(x,4)>10.0000){
                    Log.d("sensor", "X Right axis: " + x);
                    Toast.makeText(this, "Right shake detected", Toast.LENGTH_SHORT).show();

                    Intent serviceIntent = new Intent(MainActivity.this, MyServices.class);

                    serviceIntent.putExtra("coodinate", coodinate);

                    startService(serviceIntent);
                }
                else if(Round(x,4)<-10.0000){
                    Log.d("sensor", "X Left axis: " + x);
                    Toast.makeText(this, "Left shake detected", Toast.LENGTH_SHORT).show();
                }

                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                // Log.d("sensor", "diff: " + diffTime + " - speed: " + speed);
                if (speed > SHAKE_THRESHOLD) {
                    //Log.d("sensor", "shake detected w/ speed: " + speed);
                    //Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    public static float Round(float Rval, int Rpl) {
        float p = (float)Math.pow(10,Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float)tmp/p;
    }
}