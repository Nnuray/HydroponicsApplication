package com.example.hydroponics;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;


@SpringBootApplication
public class HydroponicsApplication {

    public static void main(String[] args) throws IOException, URISyntaxException {
        ClassLoader classLoader = HydroponicsApplication.class.getClassLoader();
        //File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile()); // получает ресурс из json и создает объект
        File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).toURI());

        //ключи и настроики для подключения firebase
        FileInputStream serviceAccount = new FileInputStream(file);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://fir-db-12f33-default-rtdb.firebaseio.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("/BME-280/78:21:84:E0:D9:E0/Data/Humidity");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Humidity humidity = snapshot.getValue(Humidity.class);
                            System.out.println("Max: " + humidity.getMax());
                            System.out.println("Min: " + humidity.getMin());
                            System.out.println("Optional: " + humidity.getOptional());

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
        }
        // нужно обязательно проверить чтобы инициализировать FirebaseApp только в случае отсутствия других экземпляров.
        SpringApplication.run(HydroponicsApplication.class, args);
    }

}
