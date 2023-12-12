package com.sambu.myais.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.R;
import com.sambu.myais.SplashScreen;

public class Menu2Activity extends AppCompatActivity {
    LinearLayout settings, monKecKerja, anggota, kecelakaankerja;
    TextView serialapp, koneksi, appversion, userid;

    String nomorSerial, KodeWilayah;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        settings = findViewById(R.id.settings);
        monKecKerja = findViewById(R.id.monKecKerja);
        userid = findViewById(R.id.userid);
        serialapp = findViewById(R.id.serialapp);
        koneksi = findViewById(R.id.koneksi);
        appversion = findViewById(R.id.appversion);
        anggota = findViewById(R.id.anggota);
        kecelakaankerja = findViewById(R.id.kecelakaan_kerja);

        SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        koneksi.setText(sharedPreferences.getString("pilihanjaringan", "-").toUpperCase());

        appversion.setText(getString(R.string.appversion));

        getSerialTablet();
        getUser();

        anggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu2Activity.this, AnggotaActivity.class));
            }
        });

        kecelakaankerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vld = cekDataMaster();

                if(vld.equals("valid")){
                    startActivity(new Intent(Menu2Activity.this, ListKecelakaanKerjaActivity.class));
                } else {
                    Toast.makeText(Menu2Activity.this, vld, Toast.LENGTH_SHORT).show();
                }
            }
        });

        monKecKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu2Activity.this, MonKecelakaanKerjaActivity.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu2Activity.this, Settings2Activity.class));
            }
        });
    }

    @SuppressLint({"HardwareIds", "SetTextI18n"})
    private void getSerialTablet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            nomorSerial = Build.getSerial();
            serialapp.setText(nomorSerial);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            nomorSerial = Build.SERIAL;
            serialapp.setText(nomorSerial);
        } else {
            nomorSerial = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            serialapp.setText(nomorSerial);
        }
    }

    private void getUser() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Get("User");

        if (data.getCount() == 0) {
            startActivity(new Intent(this, SplashScreen.class));
        } else {
            while (data.moveToNext()) {
                userid.setText(data.getString(0).toUpperCase());
                KodeWilayah = data.getString(1).toUpperCase();
            }
        }
    }

    private String cekDataMaster(){
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(Menu2Activity.this);
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Get("TenagaKerja");
        Cursor data2 = dataBaseAccess.Get("TipeKecelakaan");

        if(data.getCount() == 0){
            return "Data karyawan tidak ditemukan";
        }

        if(data2.getCount() == 0){
            return  "Data tipe kecelakaan tidak ditemukan";
        }

        return "valid";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }
}