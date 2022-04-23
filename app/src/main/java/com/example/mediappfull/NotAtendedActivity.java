package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotAtendedActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ListView notAList;
    private String day, month, hour, min, medicamentName, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_atended);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medicinas no atendidas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotAtendedActivity.this, ProfileActivity.class));
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        notAList = (ListView) findViewById(R.id.notAList);

        final ArrayList<String> values = new ArrayList<String>();

        databaseReference.child(auth.getCurrentUser().getUid()).child("followMedicament").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        day = ds.child("dias").getValue().toString();
                        state = ds.child("Estado").getValue().toString();
                        if(ds.child("dias").getValue().toString().equals(day)){
                            if(ds.child("Estado").getValue().toString().equals("Alarma No Atendida")){
                                medicamentName = ds.child("medicamentName").getValue().toString();
                                hour = ds.child("horas").getValue().toString();
                                min = ds.child("minutos").getValue().toString();

                                values.add(medicamentName + " " + hour + ":" + min+ "\n" +state);
                            }else{
                                Toast.makeText(NotAtendedActivity.this, "Todos los medicamentos fueron usados", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NotAtendedActivity.this, CalendarActivity.class));
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NotAtendedActivity.this, android.R.layout.simple_list_item_1,values);
                        notAList.setAdapter(adapter);
                    }
                }else{
                    Toast.makeText(NotAtendedActivity.this,"No existen datos" , Toast.LENGTH_LONG).show();
                    startActivity(new Intent(NotAtendedActivity.this, CalendarActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }
}