package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private MaterialEditText username, emailAddress, password, mobile, age, weight, height;
    private RadioGroup radioGroup;
    private Button registerBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.user);
        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mobile = findViewById(R.id.phone);
        age = findViewById(R.id.edad);
        weight = findViewById(R.id.peso);
        height = findViewById(R.id.estatura);
        radioGroup = findViewById(R.id.radioButton);
        registerBtn = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressbar);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = username.getText().toString();
                final String email = emailAddress.getText().toString();
                final String txt_password = password.getText().toString();
                final String txt_phone = mobile.getText().toString();
                final String txt_age = age.getText().toString();
                final String txt_peso = weight.getText().toString();
                final String txt_altura = height.getText().toString();
                int checkedId = radioGroup.getCheckedRadioButtonId();

                RadioButton selected_gender = radioGroup.findViewById(checkedId);

                if(selected_gender == null){
                    Toast.makeText(RegisterActivity.this, "Favor de seleccionar un genero", Toast.LENGTH_SHORT).show();
                }else{
                    final String gender = selected_gender.getText().toString();
                    if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_phone) || TextUtils.isEmpty(txt_age) || TextUtils.isEmpty(txt_altura) || TextUtils.isEmpty(txt_peso)){
                        Toast.makeText(RegisterActivity.this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                    }else{
                        Register(user_name, email, txt_password, txt_phone, txt_age, txt_peso, txt_altura, gender);
                    }
                }
            }
        });
    }

    private void Register(String user_name, String email, String txt_password, String txt_phone, String txt_age, String txt_peso, String txt_altura, String gender) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser rUser = firebaseAuth.getCurrentUser();
                    String userId = rUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("userId", userId);
                    hashMap.put("username", user_name);
                    hashMap.put("email", email);
                    hashMap.put("password", txt_password);
                    hashMap.put("phone", txt_phone);
                    hashMap.put("age", txt_age);
                    hashMap.put("weight", txt_peso);
                    hashMap.put("height", txt_altura);
                    hashMap.put("gender", gender);
                    hashMap.put("imageUrl", "default");

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}