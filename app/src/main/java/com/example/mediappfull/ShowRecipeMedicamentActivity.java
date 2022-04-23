package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Objects;

public class ShowRecipeMedicamentActivity extends AppCompatActivity {

    private DatabaseReference database;
    private FirebaseAuth auth;
    private TextView recipeName, via, gramos, medicaments;
    String medicament = "", recipe = "", key = "";
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe_medicament);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medicamento " + medicament);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowRecipeMedicamentActivity.this, ProfileActivity.class));
            }
        });

        database = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        recipeName = (TextView) findViewById(R.id.recipeName);
        via = (TextView) findViewById(R.id.via);
        gramos = (TextView) findViewById(R.id.gramos);
        medicaments = (TextView) findViewById(R.id.medicamentName);
        delete = (Button) findViewById(R.id.delete);

        database.child(auth.getCurrentUser().getUid()).child("ShowM").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        recipe = ds.child("recipeName").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child(auth.getCurrentUser().getUid()).child("showMedicament").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        medicament = ds.child("medicamentName").getValue().toString();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("Nombre medicamento" + medicament);

        database.child(auth.getCurrentUser().getUid()).child("Medicaments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(ds.child("medicamentName").getValue().toString().equals(medicament)){
                            key = ds.getKey();
                            recipeName.setText("Receta: " + recipe);
                            medicaments.setText("Medicamento: " + medicament);
                            via.setText("Via: " + ds.child("via").getValue().toString());
                            gramos.setText("Gramos: " + ds.child("gramos").getValue().toString());
                        }else{
                            Toast.makeText(ShowRecipeMedicamentActivity.this, "No hay data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child(auth.getCurrentUser().getUid()).child("Recipes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int deleteCase = 0;
                        if (snapshot.exists()) {
                            for(DataSnapshot ds: snapshot.getChildren()){
                                if(ds.child("recipeName").getValue().toString().equals(recipe)){
                                    deleteCase = 1;
                                }
                            }

                            if(deleteCase == 1){
                                Toast.makeText(ShowRecipeMedicamentActivity.this, "El medicamento: " + medicament + "\n no puede ser borrado \nporque pertenece a una receta activa", Toast.LENGTH_SHORT).show();
                            }else{
                                database.child(auth.getCurrentUser().getUid()).child("Show").removeValue();
                                database.child(auth.getCurrentUser().getUid()).child("Medicaments").child(key).removeValue();
                                database.child(auth.getCurrentUser().getUid()).child("showMedicament").removeValue();
                                Toast.makeText(ShowRecipeMedicamentActivity.this, "Medicamento: " + medicament + " borrado", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ShowRecipeMedicamentActivity.this, MedicamentListMainActivity.class));
                                finish();
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