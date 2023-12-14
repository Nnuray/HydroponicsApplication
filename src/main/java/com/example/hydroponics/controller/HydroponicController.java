package com.example.hydroponics.controller;

import com.example.hydroponics.Humidity;
import com.example.hydroponics.service.HydroponicService;
import com.google.firebase.database.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hydroponic")
public class HydroponicController {
    private final HydroponicService hydroponicService; // di

    public HydroponicController(HydroponicService hydroponicService) {
        this.hydroponicService = hydroponicService;
    }

    @GetMapping
    @RequestMapping("/humidity")
    public ResponseEntity<List<Humidity>> getHumidityData() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("/BME-280/78:21:84:E0:D9:E0/Data/Humidity");

        List<Humidity> humidityList = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Humidity humidity = new Humidity();

                        humidity.setMax(snapshot.child("max").getValue(Integer.class));
                        humidity.setMin(snapshot.child("min").getValue(Integer.class));
                        humidity.setOptional(snapshot.child("optional").getValue(Integer.class));


                        humidityList.add(humidity);
                    }
                } else {
                    System.out.println("No data available for Humidity.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error reading Humidity data: " + error.getMessage());
            }
        });

        return ResponseEntity.ok(humidityList);
    }
}

//
//    @GetMapping("/getHumidity")
//    public ResponseEntity<Humidity> getHumidityParameters(){
//        Humidity humidityParameters = hydroponicService.getHumidityParametersFromFirebase();
//        return ResponseEntity.ok(humidityParameters);
//    }
//
//    @PostMapping("/updateHumidity")
//    public ResponseEntity<String> updateHumidityParameters(@RequestBody Humidity humidityParameters) {
//        hydroponicService.updateHumidityParametersInFirebase(humidityParameters);
//        return ResponseEntity.ok("Humidity parameters updated successfully");
//    }
