package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowRecipeActivity extends AppCompatActivity {

    private TextView recipeName, doctorName;
    private String recipeNameS, docotorNameS;
    private ListView recipeList;
    private Button delete;

    private String idContact, key;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowRecipeActivity.this, RecipeActivity.class));
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        idContact = firebaseAuth.getCurrentUser().getUid();
        recipeName = (TextView) findViewById(R.id.recipeName);
        doctorName = (TextView) findViewById(R.id.doctorName);
        recipeList = (ListView) findViewById(R.id.recipeList);
        delete = findViewById(R.id.delete);

        databaseReference.child(idContact).child("ShowM").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        recipeName.setText("Nombre de la receta: \n" + ds.child("recipeName").getValue().toString());
                        //doctorName.setText("Nombre del medico: \n" + ds.child("doctorName").getValue().toString());
                        recipeNameS = ds.child("recipeName").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child(idContact).child("Recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        doctorName.setText("Nombre del medico: \n" + ds.child("doctorName").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final ArrayList<String> values = new ArrayList<String>();
        databaseReference.child(idContact).child("Medicaments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(recipeNameS.equals(ds.child("recipeName").getValue().toString())){
                            values.add(ds.child("medicamentName").getValue().toString());
                        }else{
                            Toast.makeText(ShowRecipeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowRecipeActivity.this, android.R.layout.simple_list_item_1, values);
                recipeList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String listName = (String) recipeList.getItemAtPosition(i);

                Map<String, Object> showMed = new HashMap<>();
                showMed.put("medicamentName", listName);

                databaseReference.child(idContact).child("showMedicament").push().setValue(showMed);
                startActivity(new Intent(ShowRecipeActivity.this, ShowMedicamentActivity.class));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(idContact).child("Recipes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot ds: snapshot.getChildren()){
                                if(ds.child("recipeName").getValue().equals(recipeNameS)){
                                    key = ds.getKey().toString();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Recipes").child(key).removeValue();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("number2alert").removeValue();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("followMedicament").removeValue();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Messages").removeValue();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Medicaments").removeValue();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Alarms").removeValue();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("ContactNumber").removeValue();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("idAlarm").removeValue();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("showMedicament").removeValue();
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("showM").removeValue();

                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    Intent i = new Intent(ShowRecipeActivity.this, Alarm.class);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ShowRecipeActivity.this, 0, i , PendingIntent.FLAG_IMMUTABLE);
                                    alarmManager.cancel(pendingIntent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Show").removeValue();
                Toast.makeText(ShowRecipeActivity.this, "Receta eliminada", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShowRecipeActivity.this, RecipeActivity.class));
                finish();
            }
        });
    }
}