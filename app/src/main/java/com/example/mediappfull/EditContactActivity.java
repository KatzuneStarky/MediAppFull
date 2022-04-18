package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class EditContactActivity extends AppCompatActivity {

    private MaterialEditText editname, editphone;
    private Button savecontact;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editname = findViewById(R.id.editName);
        editphone = findViewById(R.id.editPhone);
        savecontact = findViewById(R.id.savecontact);

        final String[] origenNumber = new String[1];

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Editar Contacto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditContactActivity.this, ContactsActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        savecontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("ContactNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot ds: snapshot.getChildren()){
                                origenNumber[0] =   ds.child("number").getValue().toString();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mDatabase.child(mAuth.getCurrentUser().getUid()).child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot ds: snapshot.getChildren()){
                                if(ds.child("number").getValue().toString().equals(origenNumber[0])){
                                    String nNumber = ds.getKey().toString();
                                    Map<String, Object> nNumberM = new HashMap<>();
                                    nNumberM.put("name", editname.getText().toString());
                                    nNumberM.put("number", editphone.getText().toString());
                                    mDatabase.child(mAuth.getCurrentUser().getUid()).child("Contacts").child(nNumber).updateChildren(nNumberM).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mDatabase.child(mAuth.getCurrentUser().getUid()).child("Show").removeValue();
                                                mDatabase.child(mAuth.getCurrentUser().getUid()).child("ContactNumber").removeValue();
                                                Toast.makeText(EditContactActivity.this, "Los cambios se han realizado", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(EditContactActivity.this, ContactsActivity.class));
                                            }else{
                                                Toast.makeText(EditContactActivity.this, "Los cambios no se realizaron", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(EditContactActivity.this, "El numero no existe", Toast.LENGTH_SHORT).show();
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