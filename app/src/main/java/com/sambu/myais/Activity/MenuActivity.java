package com.sambu.myais.Activity;

import androidx.appcompat.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.R;
import com.sambu.myais.SplashScreen;

public class MenuActivity extends AppCompatActivity {

    LinearLayout absensi, absensimasuk, absensiistirahat, absensimasuk2, absensipulang,historyabsen, cekabsen, anggota, settings;
    TextView namapancang, serialapp, koneksi, appversion, userid;

    Integer jmlanggota = 0, jmlkehadiran = 0;
    String nomorSerial, KodeWilayah;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

//        absensi = findViewById(R.id.absensi);
        absensimasuk = findViewById(R.id.absensimasuk);
        absensiistirahat = findViewById(R.id.absensiistirahat);
        absensimasuk2 = findViewById(R.id.absensimasuk2);
        absensipulang = findViewById(R.id.absensipulang);
        historyabsen = findViewById(R.id.historyabsen);
        cekabsen = findViewById(R.id.cekabsen);
        anggota = findViewById(R.id.anggota);
        settings = findViewById(R.id.settings);
//        namapancang = findViewById(R.id.namapancang);
        userid = findViewById(R.id.userid);
        serialapp = findViewById(R.id.serialapp);
        koneksi = findViewById(R.id.koneksi);
        appversion = findViewById(R.id.appversion);
//        jumlahanggota = findViewById(R.id.jumlahanggota);

        SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        koneksi.setText(sharedPreferences.getString("pilihanjaringan", "-").toUpperCase());

        appversion.setText(getString(R.string.appversion));

        getSerialTablet();
        getUser();
        cekAnggota();
        cekKehadiran();

        absensimasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean airplane = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
                if (airplane) {
                    Toast.makeText(MenuActivity.this, "Mohon nonaktifkan mode pesawat anda", Toast.LENGTH_SHORT).show();
                } else if (Settings.Global.getInt(getContentResolver(), "auto_time", 0) == 0) {
                    Toast.makeText(MenuActivity.this, "Pastikan jam anda di set otomatis", Toast.LENGTH_SHORT).show();
                } else if ((jmlkehadiran == 0)) {
                    GetAlertInfo("Download kehadiran dahulu!");
//                    Toast.makeText(MenuActivity.this, "Harap perbaharui kehadiran Anda", Toast.LENGTH_SHORT).show();
                } else if ((jmlanggota == 0)) {
                    GetAlertInfo("Download tenaga kerja dahulu!");
//                    Toast.makeText(MenuActivity.this, "Harap perbaharui tenaga kerja", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MenuActivity.this, ChooseAnggotaActivity.class);
                    intent.putExtra("Kode", "Absen Masuk");
                    startActivity(intent);
                }
            }
        });

        absensiistirahat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KodeWilayah.equals("KIN1") || KodeWilayah.equals("KIN2")) {
                    boolean airplane = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
                    if (airplane) {
                        Toast.makeText(MenuActivity.this, "Mohon nonaktifkan mode pesawat anda", Toast.LENGTH_SHORT).show();
                    } else if (Settings.Global.getInt(getContentResolver(), "auto_time", 0) == 0) {
                        Toast.makeText(MenuActivity.this, "Pastikan jam anda di set otomatis", Toast.LENGTH_SHORT).show();
                    } else if ((jmlkehadiran == 0)) {
                        GetAlertInfo("Download kehadiran dahulu!");
//                    Toast.makeText(MenuActivity.this, "Harap perbaharui kehadiran Anda", Toast.LENGTH_SHORT).show();
                    } else if ((jmlanggota == 0)) {
                        GetAlertInfo("Download tenaga kerja dahulu!");
//                    Toast.makeText(MenuActivity.this, "Harap perbaharui tenaga kerja", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MenuActivity.this, ChooseAnggotaActivity.class);
                        intent.putExtra("Kode", "Absen Istirahat");
                        startActivity(intent);
                    }
                } else {
                    GetAlertFailed("Anda tidak punya akses!");
                }
            }
        });

        absensimasuk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KodeWilayah.equals("KIN1") || KodeWilayah.equals("KIN2")) {
                    boolean airplane = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
                    if (airplane) {
                        Toast.makeText(MenuActivity.this, "Mohon nonaktifkan mode pesawat anda", Toast.LENGTH_SHORT).show();
                    } else if (Settings.Global.getInt(getContentResolver(), "auto_time", 0) == 0) {
                        Toast.makeText(MenuActivity.this, "Pastikan jam anda di set otomatis", Toast.LENGTH_SHORT).show();
                    }  else if ((jmlkehadiran == 0)) {
                        GetAlertInfo("Download kehadiran dahulu!");
//                    Toast.makeText(MenuActivity.this, "Harap perbaharui kehadiran Anda", Toast.LENGTH_SHORT).show();
                    } else if ((jmlanggota == 0)) {
                        GetAlertInfo("Download tenaga kerja dahulu!");
//                    Toast.makeText(MenuActivity.this, "Harap perbaharui tenaga kerja", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MenuActivity.this, ChooseAnggotaActivity.class);
                        intent.putExtra("Kode", "Absen Masuk 2");
                        startActivity(intent);
                    }
                } else {
                    GetAlertFailed("Anda tidak punya akses!");
                }
            }
        });

        absensipulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean airplane = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
                if (airplane) {
                    Toast.makeText(MenuActivity.this, "Mohon nonaktifkan mode pesawat anda", Toast.LENGTH_SHORT).show();
                } else if (Settings.Global.getInt(getContentResolver(), "auto_time", 0) == 0) {
                    Toast.makeText(MenuActivity.this, "Pastikan jam anda di set otomatis", Toast.LENGTH_SHORT).show();
                } else if ((jmlkehadiran == 0)) {
                    GetAlertInfo("Download kehadiran dahulu!");
//                    Toast.makeText(MenuActivity.this, "Harap perbaharui kehadiran Anda", Toast.LENGTH_SHORT).show();
                } else if ((jmlanggota == 0)) {
                    GetAlertInfo("Download tenaga kerja dahulu!");
//                    Toast.makeText(MenuActivity.this, "Harap perbaharui tenaga kerja", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MenuActivity.this, ChooseAnggotaActivity.class);
                    intent.putExtra("Kode", "Absen Pulang");
                    startActivity(intent);
                }
            }
        });

        historyabsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MainHistoryNewActivity.class));
            }
        });
//
        cekabsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, CekAbsensiActivity.class));
            }
        });
//
        anggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, AnggotaActivity.class));
            }
        });
//
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
            }
        });
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
//                if(data.getString(1).equals("KIN1") || data.getString(1).equals("KIN2")){
////                    absensiistirahat.setVisibility(View.GONE);
////                    absensimasuk2.setVisibility(View.GONE);
//                    absensiistirahat.setEnabled(false);
//                    absensimasuk2.setEnabled(false);
//                }
//                else {
////                    absensiistirahat.setVisibility(View.VISIBLE);
////                    absensimasuk2.setVisibility(View.VISIBLE);
//                    absensiistirahat.setEnabled(true);
//                    absensimasuk2.setEnabled(true);
//                }
            }
        }
    }

    private void getPancang() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Get("PancangTable");

        if (data.getCount() == 0) {
            startActivity(new Intent(this, SplashScreen.class));
        } else {
            while (data.moveToNext()) {
                namapancang.setText(data.getString(1));
            }
        }
    }

    private void cekAnggota() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Get("TenagaKerja");

        jmlanggota = data.getCount();
//        jumlahanggota.setText(data.getCount() + " tenaga kerja terdaftar");

//        if (data.getCount() == 0) {
//            Toast.makeText(this, "Harap perbaharui tenaga kerja Anda", Toast.LENGTH_SHORT).show();
//        }
    }

    private void cekKehadiran() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Get("Kehadiran");

        jmlkehadiran = data.getCount();

//        if (data.getCount() == 0) {
//            Toast.makeText(this, "Harap Perbaharui Kehadiran Anda", Toast.LENGTH_SHORT).show();
//        }
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

    private void GetAlertInfo(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.info_dialog,null);
        Button btnOK = view.findViewById(R.id.btnOK);
        TextView textfailed = view.findViewById(R.id.textfailed);
        builder.setCancelable(true);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        textfailed.setText(msg);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        textfailed.setText(msg);
        alertDialog.show();
    }

    private void GetAlertFailed(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.failed_dialog,null);
        Button btnOK = view.findViewById(R.id.btnOK);
        TextView textfailed = view.findViewById(R.id.textfailed);
        builder.setCancelable(true);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        textfailed.setText(msg);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        textfailed.setText(msg);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }
}