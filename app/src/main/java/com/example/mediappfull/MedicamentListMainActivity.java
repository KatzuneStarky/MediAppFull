package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MedicamentListMainActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference reference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_list_main);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lista de Medicamentos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MedicamentListMainActivity.this, ProfileActivity.class));
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listView);
        final ArrayList<String> arrayList = new ArrayList<String>();

        reference.child(auth.getCurrentUser().getUid()).child("Medicaments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        arrayList.add(ds.child("medicamentName").getValue().toString());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MedicamentListMainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String listname = (String) listView.getItemAtPosition(i);
                reference.child(auth.getCurrentUser().getUid()).child("Medicaments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot ds : snapshot.getChildren()){
                                if(listname.equals(ds.child("medicamentName").getValue().toString())){
                                    String recipeO = ds.child("recipeName").getValue().toString();

                                    Map<String, Object> show = new HashMap<>();
                                    show.put("medicamentName", listname);
                                    show.put("recipeName", recipeO);

                                    reference.child(auth.getCurrentUser().getUid()).child("Show").push().setValue(show);
                                    startActivity(new Intent(MedicamentListMainActivity.this, ShowRecipeMedicamentActivity.class));
                                    finish();
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