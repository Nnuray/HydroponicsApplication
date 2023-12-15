package com.example.hydroponics.controller;

import com.example.hydroponics.Humidity;
import com.example.hydroponics.service.FirebaseService;
import com.google.firebase.database.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.scheduling.annotation.Async;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/hydroponic")
public class HydroponicController {
    private final FirebaseService hydroponicService; // DI

    public HydroponicController(FirebaseService hydroponicService) {
        this.hydroponicService = hydroponicService;
    }

    @GetMapping
    @RequestMapping("/humidity")
    public CompletableFuture<ResponseEntity<List<Humidity>>> getHumidityData() throws NullPointerException {
        return CompletableFuture.supplyAsync(() -> {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("/BME-280/78:21:84:E0:D9:E0/Data/Humidity");

            CompletableFuture<DataSnapshot> future = new CompletableFuture<>();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    future.complete(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    future.completeExceptionally(error.toException());
                }
            });

            try {
                DataSnapshot dataSnapshot = future.get();

                List<Humidity> humidityList = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot humiditySnapshot : dataSnapshot.getChildren()) {
                        Humidity humidity = new Humidity();

                        Long max = humiditySnapshot.child("max").getValue(Long.class);
                        Long min = humiditySnapshot.child("min").getValue(Long.class);
                        Long option = humiditySnapshot.child("option").getValue(Long.class);

                        // Using setters to set the values
                        humidity.setMax(Long.valueOf(max != null ? max.intValue() : 0));
                        humidity.setMin(Long.valueOf(min != null ? min.intValue() : 0));
                        humidity.setOption(Long.valueOf(option != null ? option.intValue() : 0));

                        humidityList.add(humidity);
                    }

                } else {
                    System.out.println("No data available for Humidity.");
                }

                return ResponseEntity.ok(humidityList);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Error reading Humidity data", e);
            }
        });
    }
}



//@RestController
//@RequestMapping("/hydroponic")
//public class HydroponicController {
//
//    private final FirebaseService hydroponicService;
//
//    public HydroponicController(FirebaseService hydroponicService) {
//        this.hydroponicService = hydroponicService;
//    }
//
//    @GetMapping
//    @RequestMapping("/humidity")
//    public CompletableFuture<ResponseEntity<List<Humidity>>> getHumidityData() {
//        DatabaseReference ref = FirebaseDatabase.getInstance()
//                .getReference("/BME-280/78:21:84:E0:D9:E0/Data/Humidity");
//
//        return CompletableFuture
//                .supplyAsync(() -> {
//                    CompletableFuture<DataSnapshot> future = new CompletableFuture<>();
//                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            future.complete(dataSnapshot);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError error) {
//                            future.completeExceptionally(error.toException());
//                        }
//                    });
//                    return future.join();
//                })
//                .orTimeout(5, TimeUnit.SECONDS)
//                .thenApply(dataSnapshot -> {
//                    List<Humidity> humidityList = new ArrayList<>();
//
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot humiditySnapshot : dataSnapshot.getChildren()) {
//                            Humidity humidity = new Humidity();
//
//                            Integer max = humiditySnapshot.child("max").getValue(Integer.class);
//                            Integer min = humiditySnapshot.child("min").getValue(Integer.class);
//                            Integer option = humiditySnapshot.child("option").getValue(Integer.class);
//
//                            humidity.setMax(max != null ? max : 12);
//                            humidity.setMin(min != null ? min : 10);
//                            humidity.setOption(option != null ? option : 11);
//
//                            humidityList.add(humidity);
//                        }
//                    } else {
//                        System.out.println("No data available for Humidity.");
//                    }
//
//                    return ResponseEntity.ok(humidityList);
//                });
//    }
//}
