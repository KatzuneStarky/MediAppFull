package com.example.mediappfull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView title, subtitle;
    private static int splash_timeout = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.titletv);
        subtitle = findViewById(R.id.subtitletv);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, splash_timeout);
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim2);
        title.startAnimation(animation);

        Animation animation2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim1);
        subtitle.startAnimation(animation2);
    }
}