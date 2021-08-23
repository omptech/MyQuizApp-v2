package com.omyser.myquiz;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.appName);

        Typeface typeface = ResourcesCompat.getFont(this,R.font.blacklist);
        appName.setTypeface(typeface);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myanim);
        appName.setAnimation(anim);



        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();



    }


}
