package com.sambu.myais;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.sambu.myais.Activity.LoginActivity;
import com.sambu.myais.Activity.MenuActivity;
import com.sambu.myais.DataBase.DataBaseAccess;

public class SplashScreen extends AppCompatActivity {

    TextView appversion;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        appversion = findViewById(R.id.appversion);

        appversion.setText("App Version : " + getString(R.string.appversion));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(SplashScreen.this.getApplicationContext());
                dataBaseAccess.open();
                if (dataBaseAccess.Get("User").getCount() == 0) {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(SplashScreen.this, MenuActivity.class));
                }
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
    }
}