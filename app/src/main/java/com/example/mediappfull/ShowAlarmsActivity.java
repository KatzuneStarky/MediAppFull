package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class ShowAlarmsActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference reference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarms);

        reference =FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        listView = (ListView) findViewById(R.id.listView);
        listView.setCacheColorHint(Color.RED);
        final ArrayList<String> values = new ArrayList<String>();
        reference.child(auth.getCurrentUser().getUid()).child("Alarms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String hour = ds.child("Hora").getValue().toString();
                        String min = ds.child("Minutos").getValue().toString();
                        String medicamentName = ds.child("NombreMedicamento").getValue().toString();
                        values.add("Hora de la alarma: " + hour + ":" + min);
                    }
                }else{
                    Toast.makeText(ShowAlarmsActivity.this, "No", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShowAlarmsActivity.this, ProfileActivity.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowAlarmsActivity.this, android.R.layout.simple_list_item_1,values);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}