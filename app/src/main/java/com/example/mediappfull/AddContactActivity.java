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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {

    private TextView name, phone;
    private Button addContact;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agregar Contactos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddContactActivity.this, ContactsActivity.class));
            }
        });

        name = findViewById(R.id.nameC);
        phone = findViewById(R.id.phone);
        addContact = findViewById(R.id.addcontact);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameContact = name.getText().toString();
                String phoneContact = phone.getText().toString();

                if(!nameContact.isEmpty() || !phoneContact.isEmpty()){
                    Map<String, Object> contactMap = new HashMap<>();
                    contactMap.put("name", nameContact);
                    contactMap.put("number", phoneContact);

                    String idContact = mAuth.getCurrentUser().getUid();

                    mDatabase.child(idContact).child("Contacts").push().setValue(contactMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddContactActivity.this, "Contacto Agregado", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddContactActivity.this, ContactsActivity.class));
                            }
                        }
                    });

                }else{
                    Toast.makeText(AddContactActivity.this, "Favor de rellenar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}