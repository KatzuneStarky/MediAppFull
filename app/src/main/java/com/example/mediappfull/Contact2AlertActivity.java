package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Contact2AlertActivity extends AppCompatActivity {

    private Button añadir;
    private Spinner spinnerContact;
    private TextView contactN;
    private String selectedName;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    final ArrayList<String> values = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact2_alert);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        contactN = findViewById(R.id.nameC);
        spinnerContact = findViewById(R.id.spinnerContact);
        añadir = findViewById(R.id.addcontact);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contacto a Notificar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Contact2AlertActivity.this, AddRecipeActivity.class));
            }
        });

        loadContacts();

        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        values.clear();
                        if(snapshot.exists()){
                            for(DataSnapshot ds : snapshot.getChildren()){
                                if(ds.child("number").getValue().toString().equals(contactN.getText().toString())){
                                    Map<String, Object> number2alert = new HashMap<>();
                                    number2alert.put("number", ds.child("number").getValue().toString());

                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("number2alert").push().setValue(number2alert);
                                    Toast.makeText(Contact2AlertActivity.this, "El contacto: " + contactN.getText().toString() + " sera alertado", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Contact2AlertActivity.this, ProfileActivity.class));
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

    private void loadContacts() {
        final List<Contacts> contacts = new ArrayList<>();

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String name = ds.child("name").getValue().toString();
                        String number = ds.child("number").getValue().toString();
                        contacts.add(new Contacts(name, number));
                    }

                    if(contacts.isEmpty()){
                        Toast.makeText(Contact2AlertActivity.this, "No Tienes Contactos Registrados", Toast.LENGTH_SHORT).show();
                    }else{
                        ArrayAdapter<Contacts> arrayAdapter = new ArrayAdapter<>(Contact2AlertActivity.this, android.R.layout.simple_dropdown_item_1line, contacts);
                        spinnerContact.setAdapter(arrayAdapter);

                        spinnerContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedName = adapterView.getItemAtPosition(i).toString();
                                contactN.setText(selectedName);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}