package com.example.mediappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private CalendarView calendar1;
    private String state;
    private ImageButton dotred, dotgreen, dotyellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Calendario de seguimiento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalendarActivity.this, ProfileActivity.class));
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        dotred = findViewById(R.id.notAtended);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar1 = (CalendarView) findViewById(R.id.calendar);

        databaseReference.child(auth.getCurrentUser().getUid()).child("followMedicament").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String day = ds.child("dias").getValue().toString();
                        String month = ds.child("mes").getValue().toString();
                        int year = calendar.get(Calendar.YEAR);

                        String date = "" + day + "/" + month + "/" + year + "";
                        String parts[] = date.split("/");

                        int dayC = Integer.parseInt(parts[0]);
                        int monthC = Integer.parseInt(parts[1]);
                        int yearC = Integer.parseInt(parts[2]);

                        calendar.set(Calendar.YEAR, yearC);
                        calendar.set(Calendar.MONTH, monthC);
                        calendar.set(Calendar.DAY_OF_MONTH, dayC);

                        long millis = calendar.getTimeInMillis();
                        calendar1.setDate(millis, true, true);
                    }
                }else{
                    Toast.makeText(CalendarActivity.this, "No existe informacion por el momento", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CalendarActivity.this, ProfileActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        calendar1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                startActivity(new Intent(CalendarActivity.this, FollowMedActivity.class));
            }
        });

        dotred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalendarActivity.this, NotAtendedActivity.class));
            }
        });
    }
}