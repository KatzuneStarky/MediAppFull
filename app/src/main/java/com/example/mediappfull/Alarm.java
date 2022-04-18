package com.example.mediappfull;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Alarm extends BroadcastReceiver {
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    public void onReceive(Context context, Intent intent) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        int ID = intent.getIntExtra("id", -1);
        Map<String, Object> idMedicine = new HashMap<>();
        idMedicine.put("idMedicine", ID);

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("idAlarm").push().setValue(idMedicine);

        vibrator.vibrate(1000);
        Intent i = new Intent(context.getApplicationContext(), ShowAlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
