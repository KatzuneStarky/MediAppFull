package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.SimpleFormatter;

import pl.droidsonroids.gif.GifImageView;

public class ShowAlarmActivity extends AppCompatActivity {

    private Button quitarAlarma;
    private TextView medTomar;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private String quantity, via, name, number, medicamentName, idAlarm;
    ArrayList<String> alertNumberArr = new ArrayList<String>();
    private int i = 0;
    private int SMS_PERMISSION_CODE= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm);

        quitarAlarma = (Button) findViewById(R.id.quitarAlarma);
        medTomar = (TextView) findViewById(R.id.medTomar);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.start();

        reference.child(auth.getCurrentUser().getUid()).child("idAlarm").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        idAlarm = ds.child("idMedicine").getValue().toString();
                    }
                    System.out.println(idAlarm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        reference.child(auth.getCurrentUser().getUid()).child("Medicaments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        if(ds.child("AlarmCode").getValue().toString().equals(idAlarm)){
                            quantity = ds.child("cantidad").getValue().toString();
                            medicamentName = ds.child("medicamentName").getValue().toString();
                            via = ds.child("via").getValue().toString();
                            medTomar.setText("Toca la medicina: " + medicamentName
                                    + "\nEn la siguiente dosis: " + quantity);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        reference.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        reference.child(auth.getCurrentUser().getUid()).child("number2alert").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        number = ds.child("number").getValue().toString();
                        alertNumberArr.add(number);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        quitarAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=1;
                System.out.println("El valor es: "+ i);
                mediaPlayer.stop();
                startActivity(new Intent(ShowAlarmActivity.this, ProfileActivity.class));
                finish();
            }
        });

        Handler handler = new Handler();
        long miliseconds = 20000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(i == 1){
                    Map<String, Object> map = new HashMap<>();

                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    int min = calendar.get(Calendar.MINUTE);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH)+1;

                    map.put("Estado", "Alarma Atendida");
                    map.put("Hora", hour);
                    map.put("Minutos", min);
                    map.put("Dia", day);
                    map.put("Mes", month);
                    map.put("NombreMedicamento", medicamentName);

                    reference.child(auth.getCurrentUser().getUid()).child("followMedicament").push().setValue(map);
                    Map<String, Object> AlarmMap = new HashMap<>();
                    AlarmMap.put("Hora", hour);
                    AlarmMap.put("Minutos", min);
                    AlarmMap.put("NombreMedicamento", medicamentName);
                    reference.child(auth.getCurrentUser().getUid()).child("Alarms").push().setValue(AlarmMap);
                    i = 0;
                    System.out.println("El valor es: "+ i);
                    reference.child(auth.getCurrentUser().getUid()).child("idAlarm").removeValue();
                }else if(i== 0){
                    Map<String, Object> map = new HashMap<>();

                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    int min = calendar.get(Calendar.MINUTE);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH)+1;

                    map.put("Estado", "Alarma No Atendida");
                    map.put("Hora", hour);
                    map.put("Minutos", min);
                    map.put("Dia", day);
                    map.put("Mes", month);
                    map.put("NombreMedicamento", medicamentName);
                    System.out.println("El valor es: "+ i);

                    reference.child(auth.getCurrentUser().getUid()).child("followMedicament").push().setValue(map);
                    Map<String, Object> AlarmMap = new HashMap<>();
                    AlarmMap.put("Hora", hour);
                    AlarmMap.put("Minutos", min);
                    AlarmMap.put("NombreMedicamento", medicamentName);
                    reference.child(auth.getCurrentUser().getUid()).child("Alarms").push().setValue(AlarmMap);
                    i = 0;
                    reference.child(auth.getCurrentUser().getUid()).child("idAlarm").removeValue();

                    for(i = 0; i < alertNumberArr.size(); i ++){
                        Map<String, Object> mesageMap = new HashMap<>();
                        String phone = alertNumberArr.get(i);
                        String message = "El usuario: " + name + " no se a medicado" +
                                "\nRecuerde consultar la receta en nuestra app";
                        SmsManager smsManager = SmsManager.getDefault();
                        System.out.println(phone);

                        if(ContextCompat.checkSelfPermission(ShowAlarmActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                            System.out.println(phone);
                            smsManager.sendTextMessage(phone, null, message,null, null);
                            System.out.println(smsManager);
                            Toast.makeText(ShowAlarmActivity.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        }else{
                            //Toast.makeText(ShowAlarmActivity.this, "Mensaje no enviado", Toast.LENGTH_SHORT).show();
                            obtenerPermiso();
                            System.out.println(smsManager);
                        }

                        mesageMap.put("phone", phone);
                        mesageMap.put("message", message);
                        reference.child(auth.getCurrentUser().getUid()).child("Messages").push().setValue(mesageMap);

                        reference.child(auth.getCurrentUser().getUid()).child("idAlarm");
                        finish();
                    }
                }
                finish();
                reference.child(auth.getCurrentUser().getUid()).child("idAlarm");
            }
        }, miliseconds);
    }

    private void obtenerPermiso() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
            new AlertDialog.Builder(this)
                    .setTitle("Permiso SMS")
                    .setMessage("Se requiere este permiso")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(ShowAlarmActivity.this, new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}