package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
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

public class FollowMedActivity extends AppCompatActivity {

    private ListView followList;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String medicamentName;
    final ArrayList<String> values = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_med);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Seguimiento de receta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FollowMedActivity.this, ProfileActivity.class));
            }
        });

        followList = (ListView) findViewById(R.id.followList);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(auth.getCurrentUser().getUid()).child("followMedicament").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        medicamentName = ds.child("medicamentName").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        databaseReference.child(auth.getCurrentUser().getUid()).child("followMedicament").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String day = ds.child("dias").getValue().toString();
                        String hours = ds.child("horas").getValue().toString();
                        String min = ds.child("minutos").getValue().toString();
                        String state = ds.child("Estado").getValue().toString();

                        values.add("Medicamento: " + medicamentName +
                                "\nDia: " + day + ", Hora: " + hours + ":" + min
                                + "\nEstado: " + state);
                    }
                }else{
                    Toast.makeText(FollowMedActivity.this, "No cuenta con un seguimiento activo", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FollowMedActivity.this, ProfileActivity.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FollowMedActivity.this, android.R.layout.simple_list_item_1, values);
                followList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        followList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                databaseReference.child(auth.getCurrentUser().getUid()).child("followMedicament").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String state = ds.child("Estado").getValue().toString();
                                if(state.equals("Atendida")){
                                    Toast.makeText(FollowMedActivity.this, Html.fromHtml("<font color='#04AF21'><b>" + state + "</b></font>"), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(FollowMedActivity.this, Html.fromHtml("<font color='#D31303'><b>" + state + "</b></font>"), Toast.LENGTH_SHORT).show();
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