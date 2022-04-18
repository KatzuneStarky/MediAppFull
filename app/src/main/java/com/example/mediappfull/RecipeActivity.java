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

public class RecipeActivity extends AppCompatActivity {

    private Button addRecipe;
    private ListView recipeList;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String selectedName, selectedRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recetas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecipeActivity.this, ProfileActivity.class));
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        addRecipe = findViewById(R.id.btnaddrecipe);
        recipeList = findViewById(R.id.recipeList);
        final ArrayList<String> values = new ArrayList<String>();

        databaseReference.child(auth.getCurrentUser().getUid()).child("Recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        values.add(ds.child("recipeName").getValue().toString());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecipeActivity.this, android.R.layout.simple_list_item_1, values);
                recipeList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String listName = (String)recipeList.getItemAtPosition(i);

                databaseReference.child(auth.getCurrentUser().getUid()).child("Recipes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        values.clear();
                        if(snapshot.exists()){
                            for(DataSnapshot ds : snapshot.getChildren()){
                                if(listName.equals(ds.child("recipeName").getValue().toString())){
                                    selectedName = (ds.child("doctorName").getValue().toString());
                                    selectedRecipe = (ds.child("recipeName").getValue().toString());

                                    Map<String, Object> recipeData = new HashMap<>();

                                    recipeData.put("recipeName", selectedRecipe);
                                    recipeData.put("doctorName", selectedName);

                                    databaseReference.child(auth.getCurrentUser().getUid()).child("Show").push().setValue(recipeData);
                                    startActivity(new Intent(RecipeActivity.this, ShowRecipeActivity.class));
                                }
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecipeActivity.this, android.R.layout.simple_list_item_1,values);
                        recipeList.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(auth.getCurrentUser().getUid()).child("Recipe").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            databaseReference.child(auth.getCurrentUser().getUid()).child("followMedicament").removeValue();
                            databaseReference.child(auth.getCurrentUser().getUid()).child("futureAlarm").removeValue();
                            databaseReference.child(auth.getCurrentUser().getUid()).child("Alarm").removeValue();
                            databaseReference.child(auth.getCurrentUser().getUid()).child("numberContact").removeValue();
                            databaseReference.child(auth.getCurrentUser().getUid()).child("follow").removeValue();

                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent i = new Intent(RecipeActivity.this, Alarm.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(RecipeActivity.this, 0, i, PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.cancel(pendingIntent);

                            startActivity(new Intent(RecipeActivity.this, AddRecipeActivity.class));
                        }else{
                            startActivity(new Intent(RecipeActivity.this, AddRecipeActivity.class));
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