package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddRecipeActivity extends AppCompatActivity {

    private MaterialEditText recipeName, doctorName, medicamentName, gramos, cantidad, dias, horas;
    private RadioGroup radioGroup;
    private Button addMore, saveRecipe;
    private int pass = 0;
    private String idContact;
    private Random random = new Random();
    private int Nu = 0;
    //private static ArrayList<Recipe> Recipe = new ArrayList<>();
    private String viaTip;
    private String doctorNameS, medicamentNameS, gramosS, cantidadS, diasS, horasS, recipeNameS;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agregar Receta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddRecipeActivity.this, RecipeActivity.class));
            }
        });

        recipeName = (MaterialEditText) findViewById(R.id.recipeName);
        doctorName = (MaterialEditText) findViewById(R.id.doctorName);
        medicamentName = (MaterialEditText) findViewById(R.id.medicamentName);
        gramos = (MaterialEditText) findViewById(R.id.gramos);
        cantidad = (MaterialEditText) findViewById(R.id.cantidad);
        dias = (MaterialEditText) findViewById(R.id.dias);
        horas = (MaterialEditText) findViewById(R.id.horas);
        radioGroup = (RadioGroup) findViewById(R.id.radioButton);
        addMore = findViewById(R.id.addMedicament);
        saveRecipe = findViewById(R.id.btnaddMedicament);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        idContact = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Show").removeValue();

        saveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Guardado", Toast.LENGTH_SHORT).show();
                RefillRecipe();
                startActivity(new Intent(AddRecipeActivity.this, Contact2AlertActivity.class));
                finish();
            }
        });

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeNameS = recipeName.getText().toString();
                doctorNameS = doctorName.getText().toString();
                medicamentNameS = medicamentName.getText().toString();
                gramosS = gramos.getText().toString();
                cantidadS = cantidad.getText().toString();
                diasS = dias.getText().toString();
                horasS = horas.getText().toString();

                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selected_via = radioGroup.findViewById(checkedId);
                if (selected_via == null) {
                    Toast.makeText(AddRecipeActivity.this, "Favor de seleccionar un tipo de via", Toast.LENGTH_SHORT).show();
                } else {
                    viaTip = selected_via.getText().toString();
                }

                if(recipeNameS.isEmpty() || doctorNameS.isEmpty() || medicamentNameS.isEmpty() ||
                        gramosS.isEmpty() || cantidadS.isEmpty() || diasS.isEmpty() || horasS.isEmpty()){
                    Toast.makeText(AddRecipeActivity.this, "Todos los campos deben ser rellenados", Toast.LENGTH_SHORT).show();
                }else{
                    Nu = random.nextInt(1000)+1;
                    RefillRecipe();

                    Map<String, Object> followMedicament = new HashMap<>();

                    followMedicament.put("medicamentName", medicamentNameS);
                    followMedicament.put("horas", horasS);
                    followMedicament.put("idAlarm", Nu);
                    followMedicament.put("dias", diasS);

                    databaseReference.child(idContact).child("followMedicament").push().setValue(followMedicament);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Calendar calendar = Calendar.getInstance();
                    Intent i = new Intent(AddRecipeActivity.this, Alarm.class);
                    i.putExtra("ID", Nu);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddRecipeActivity.this, Nu, i, PendingIntent.FLAG_IMMUTABLE);
                    int min = Integer.parseInt(horasS);
                    int totalMin = calendar.get(Calendar.MINUTE) + min;

                    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                    calendar.set(Calendar.MINUTE, totalMin);

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            1000 * 60 * min, pendingIntent);

                }
            }
        });
    }

    private void RefillRecipe() {
        if(pass == 0){

            Map<String, Object> recipeListTodo = new HashMap<>();
            recipeListTodo.put("doctorName", doctorNameS);
            recipeListTodo.put("recipeName", recipeNameS);

            databaseReference.child(idContact).child("Recipes").push().setValue(recipeListTodo);
            Map<String, Object> recipeList = new HashMap<>();

            recipeList.put("AlarmCode", Nu);
            recipeList.put("recipeName", recipeNameS);
            recipeList.put("medicamentName", medicamentNameS);
            recipeList.put("gramos", gramosS);
            recipeList.put("cantidad", cantidadS);
            recipeList.put("horas", horasS);
            recipeList.put("dias", diasS);
            recipeList.put("via", viaTip);

            databaseReference.child(idContact).child("Medicaments").push().setValue(recipeList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddRecipeActivity.this, "Medicamento añadido", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            pass = 1;
        }else{
            Map<String, Object> recipeList = new HashMap<>();

            recipeList.put("AlarmCode", Nu);
            recipeList.put("recipeName", recipeNameS);
            recipeList.put("medicamentName", medicamentNameS);
            recipeList.put("gramos", gramosS);
            recipeList.put("cantidad", cantidadS);
            recipeList.put("horas", horasS);
            recipeList.put("dias", diasS);
            recipeList.put("via", viaTip);

            databaseReference.child(idContact).child("Medicaments").push().setValue(recipeList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddRecipeActivity.this, "Medicamento añadido", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}