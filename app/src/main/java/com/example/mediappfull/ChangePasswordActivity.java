package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ChangePasswordActivity extends AppCompatActivity {

    private MaterialEditText Opassword, NPassword, CPassword;
    private Button changePsw;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cambiar contraseña");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePasswordActivity.this, ProfileActivity.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Opassword = findViewById(R.id.Opassword);
        NPassword = findViewById(R.id.Npassword);
        CPassword = findViewById(R.id.Cpassword);
        changePsw = findViewById(R.id.actPassword);
        progressBar = findViewById(R.id.progressbar);

        changePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtOldPsw = Opassword.getText().toString();
                String txtNexPsw = NPassword.getText().toString();
                String txtConfirmPsw = CPassword.getText().toString();

                if(txtOldPsw.isEmpty() || txtNexPsw.isEmpty() || txtConfirmPsw.isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "Todos los campos deben ser rellenados", Toast.LENGTH_SHORT).show();
                }else if(txtNexPsw.length() < 6){
                    Toast.makeText(ChangePasswordActivity.this, "La nueva contraseña debe contener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
                }else if(!txtConfirmPsw.equals(txtNexPsw)){
                    Toast.makeText(ChangePasswordActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }else{
                    changePassword(txtOldPsw, txtNexPsw);
                }
            }
        });
    }

    private void changePassword(String txtOldPsw, String txtNexPsw) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), txtOldPsw);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseUser.updatePassword(txtNexPsw).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                firebaseAuth.signOut();
                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}