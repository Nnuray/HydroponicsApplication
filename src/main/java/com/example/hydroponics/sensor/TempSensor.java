package com.example.hydroponics.sensor;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TempSensor implements Sensor{
    private DatabaseReference tempReference;
    private double minTemp;
    private double maxTemp;
    private double optimalTemp;

    @Override
    public double getValue() {
        return 0;
    }

//    public void setValueMax(double maxTemp) {
//        this.maxTemp = maxTemp;
//    }

    @Override
    public void update() {

    }

    @Override
    public void execute() {
        tempReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double currentTemp = dataSnapshot.getValue(Double.class);
//                if (currentTemp < minTemp) {
//                    tempReference.setValue(optimalTemp);
//                } else if (currentTemp > maxTemp) {
//                    tempReference.setValue(optimalTemp);
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public TempSensor() throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream("serviceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(fileInputStream))
                .setDatabaseUrl("https://fir-db-12f33-default-rtdb.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(options);
        this.tempReference = FirebaseDatabase.getInstance().getReference("BME-280/78:21:84:E0:D9:E0/Data/Temperature");


        // Получение значения min, max и optimal из Firebase
        tempReference.child("min").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                minTemp = dataSnapshot.getValue(Double.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки при чтении данных из Firebase
            }
        });

        tempReference.child("max").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                maxTemp = dataSnapshot.getValue(Double.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки при чтении данных из Firebase
            }
        });

        tempReference.child("optimal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                optimalTemp = dataSnapshot.getValue(Double.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки при чтении данных из Firebase
            }
        });
    }
}
