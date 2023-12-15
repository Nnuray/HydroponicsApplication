package com.example.hydroponics.service;

import com.google.firebase.database.*;
import org.springframework.stereotype.Service;
@Service
public class FirebaseService {

    public Long updateDate(String uid, String uidFromPush) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(uid)
                .child("/BME-280/78:21:84:E0:D9:E0/Data/")
                .child(uidFromPush)
                .child("Humidity");

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long p = mutableData.getValue(Long.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                // Set value to the current time
                p = System.currentTimeMillis();

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }


            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError == null) {
                    // Everything is OK
                    System.out.println("everything is ok");
                } else {
                    // An error occurred: databaseError.toException()
                    System.out.println("error");
                }
            }
        });

        // Returning the updated value; you might need to adapt this based on your requirements
        return System.currentTimeMillis();
    }
}
