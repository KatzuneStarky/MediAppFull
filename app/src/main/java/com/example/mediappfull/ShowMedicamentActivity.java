package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowMedicamentActivity extends AppCompatActivity {

    private TextView gramos, via, dias, horas, cantidad, medicineName;
    private String grams, viaT, days, hours, quantity, medName, idUser, aux;
    private ImageButton btnMuted;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_medicament);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Muestra de medicamentos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowMedicamentActivity.this, ShowRecipeActivity.class));
            }
        });

        database = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        medicineName = (TextView) findViewById(R.id.medicamentName);
        gramos = (TextView) findViewById(R.id.grams);
        via = (TextView) findViewById(R.id.viaT);
        dias = (TextView) findViewById(R.id.dias);
        horas = (TextView) findViewById(R.id.horas);
        cantidad = (TextView) findViewById(R.id.cantidad);
        btnMuted = (ImageButton) findViewById(R.id.btnMuted);
        idUser = auth.getCurrentUser().getUid();

        database.child(idUser).child("showMedicament").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        medicineName.setText(ds.child("medicamentName").getValue().toString());
                        aux = ds.child("medicamentName").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child(idUser).child("Medicaments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(aux.equals(ds.child("medicamentName").getValue().toString())){
                            gramos.setText(ds.child("gramos").getValue().toString());
                            via.setText(ds.child("via").getValue().toString());
                            dias.setText(ds.child("dias").getValue().toString() + " dias");
                            horas.setText(ds.child("horas").getValue().toString() + " horas");
                            cantidad.setText(ds.child("cantidad").getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnMuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child(idUser).child("Medicaments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot ds: snapshot.getChildren()){
                                if(ds.child("medicamentName").getValue().toString().equals(medicineName.getText().toString())){
                                    String code = ds.child("AlarmCode").getValue().toString();
                                    int codeA = Integer.parseInt(code);
                                    Intent i = new Intent(ShowMedicamentActivity.this, Alarm.class);
                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ShowMedicamentActivity.this, codeA, i, PendingIntent.FLAG_IMMUTABLE);
                                    alarmManager.cancel(pendingIntent);
                                    Toast.makeText(ShowMedicamentActivity.this, "Medicamento silenciado: " + medicineName.getText().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}