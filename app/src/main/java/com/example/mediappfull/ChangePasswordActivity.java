package com.example.mediappfull;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Opassword = findViewById(R.id.Opassword);
        NPassword = findViewById(R.id.Npassword);
        CPassword = findViewById(R.id.Cpassword);
        changePsw = findViewById(R.id.actPassword);
        progressBar = findViewById(R.id.progressbar);
    }
}