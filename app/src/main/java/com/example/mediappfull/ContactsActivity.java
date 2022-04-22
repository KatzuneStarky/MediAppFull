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

public class ContactsActivity extends AppCompatActivity {

    private Button btnaddcontact;
    private ListView contactList;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        btnaddcontact = findViewById(R.id.btnaddcontact);
        mAuth = FirebaseAuth.getInstance();
        contactList = (ListView) findViewById(R.id.contactList);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final ArrayList<String> values = new ArrayList<String>();

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contactos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsActivity.this, ProfileActivity.class));
            }
        });

        mDatabase.child(mAuth.getCurrentUser().getUid()).child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        values.add(ds.child("name").getValue().toString());
                    }
                }else{
                    Toast.makeText(ContactsActivity.this, "No cuenta con contactos aun!", Toast.LENGTH_SHORT).show();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ContactsActivity.this, android.R.layout.simple_list_item_1, values);
                contactList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactsActivity.this, "No cuenta con contactos aun!", Toast.LENGTH_SHORT).show();
            }
        });

        btnaddcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsActivity.this, AddContactActivity.class));
            }
        });

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String listName = (String)contactList.getItemAtPosition(i);

                mDatabase.child(mAuth.getCurrentUser().getUid()).child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        values.clear();
                        if (snapshot.exists()) {
                            for(DataSnapshot ds: snapshot.getChildren()){
                                if(listName.equals(ds.child("name").getValue().toString())) {
                                    String name = (ds.child("name").getValue().toString());
                                    String number = (ds.child("number").getValue().toString());
                                    String idContact = mAuth.getCurrentUser().getUid();

                                    Map<String, Object> contactsMap = new HashMap<>();

                                    contactsMap.put("name", name);
                                    contactsMap.put("number", number);
                                    contactsMap.put("id", ds.getKey().toString());

                                    mDatabase.child(idContact).child("Show").push().setValue(contactsMap);
                                    startActivity(new Intent(ContactsActivity.this, ShowContactActivity.class));
                                    
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