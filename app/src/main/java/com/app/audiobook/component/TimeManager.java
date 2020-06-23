package com.app.audiobook.component;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeManager {

    public static double offsetTime = 0f;

    // загружает смещение времени пользователя относительно firebase
    public static void loadTimeOffset() {
        FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        double offset = dataSnapshot.getValue(Double.class);

                        offsetTime = offset;

                        Log.v("offset", String.valueOf(offset));

                        long curTime = (long) (System.currentTimeMillis() + offset);

                        Log.v("offset", String.valueOf(curTime));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public static long getCurrentTime() {
        return (long) (System.currentTimeMillis() + offsetTime);
    }

    public static String getCurrentTimeString() {
        return convertToString(getCurrentTime());
    }

    public static String convertToString(long currentTime) {
        Date dateNow = new Date(currentTime);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");

        return formatForDateNow.format(dateNow);
    }

    public static long convertToLong(String currentTime) {
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");

        try {
            Date date = formatForDateNow.parse(currentTime);

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }


}
