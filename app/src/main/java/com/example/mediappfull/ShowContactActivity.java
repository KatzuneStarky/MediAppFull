package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ShowContactActivity extends AppCompatActivity {

    private TextView Mname, Mphone;
    private ImageButton editbtn, deletebtn;
    private String key;
    private String showKey;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        Mname  = findViewById(R.id.Mname);
        Mphone = findViewById(R.id.Mphone);

        editbtn = findViewById(R.id.btneditC);
        deletebtn = findViewById(R.id.btnborrarC);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mostrar Contacto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowContactActivity.this, ContactsActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mDatabase.child(mAuth.getCurrentUser().getUid()).child("Show").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        key = ds.child("id").getValue().toString();
                        showKey =ds.getKey().toString();

                        Mname.setText(ds.child("name").getValue().toString());
                        Mphone.setText(ds.child("number").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("Show").child(showKey).removeValue();
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("Contacts").child(key).removeValue();

                startActivity(new Intent(ShowContactActivity.this, ContactsActivity.class));
                finish();
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> contact = new HashMap<>();
                contact.put("number", Mphone.getText().toString());
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("ContactNumber").push().setValue(contact);
                startActivity(new Intent(ShowContactActivity.this, EditContactActivity.class));
                finish();
            }
        });
    }
}