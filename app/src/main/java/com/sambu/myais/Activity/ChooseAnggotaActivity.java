package com.sambu.myais.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.ListView.AdapterAnggota;
import com.sambu.myais.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChooseAnggotaActivity extends AppCompatActivity {

    ListView listAnggota;
    TextView nomember, chooseTK;
    ProgressBar ProgressTK;

    String kodeAbsen = "", tglNow;

    Handler handler = new Handler();
    Runnable runnable;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdfAPI = new SimpleDateFormat("yyyy-MM-dd");

    //map
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_anggota);

        listAnggota = findViewById(R.id.listAnggota);
        nomember = findViewById(R.id.nomember);
        chooseTK = findViewById(R.id.chooseTK);
        ProgressTK = findViewById(R.id.ProgressTK);

        getDateAPI();

        try{
            String kode = getIntent().getExtras().getString("Kode");
            if(kode.equals("Absen Masuk")){
                kodeAbsen = "Absen Masuk";
                getSubPersilKelapaMasuk();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ProgressTK.setVisibility(View.GONE);
                        getTenagaKerja();
                    }
                }, 500);
            }
            else if (kode.equals("Absen Istirahat")){
                kodeAbsen = "Absen Istirahat";
                getSubPersilKelapa();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ProgressTK.setVisibility(View.GONE);
                        getTenagaKerja();
                    }
                }, 500);
            }else if (kode.equals("Absen Masuk 2")){
                kodeAbsen = "Absen Masuk 2";
                getSubPersilKelapa();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ProgressTK.setVisibility(View.GONE);
                        getTenagaKerja();
                    }
                }, 500);
            }
            else if (kode.equals("Absen Pulang")){
                kodeAbsen = "Absen Pulang";
                getSubPersilKelapa();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ProgressTK.setVisibility(View.GONE);
                        getTenagaKerja();
                    }
                }, 500);
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChooseAnggotaActivity.this, MenuActivity.class));
    }

    private void getDateAPI() {
        if (Settings.Global.getInt(getContentResolver(), "auto_time", 0) == 0) {
            Toast.makeText(ChooseAnggotaActivity.this, "Pastikan jam anda di set otomatis", Toast.LENGTH_SHORT).show();
        }
        tglNow = sdfAPI.format(new Date());
    }

    private void getSubPersilKelapaMasuk() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Where("SubPersilKelapa", "LokasiAbsensiMasuk = 1 AND ActiveLokasiMasuk = 1");

        if (data.getCount() == 0) {
            chooseTK.setText("Persil belum di set");
        } else {
            while (data.moveToNext()) {
                chooseTK.setText(kodeAbsen + " [" + data.getString(0).toUpperCase() + "]"  );
            }
        }
    }

    private void getSubPersilKelapa() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Where("SubPersilKelapa", "Active = 1");

        if (data.getCount() == 0) {
            chooseTK.setText("Persil belum di set");
        } else {
            while (data.moveToNext()) {
                chooseTK.setText(kodeAbsen + " [" + data.getString(0).toUpperCase() + "]"  );
            }
        }
    }

    private void getTenagaKerja() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();

        String whr = "RegNo NOT IN (SELECT RegNo FROM Absensi WHERE KodeAbsen ='" + kodeAbsen + "' AND SUBSTR(Tanggal, 1, 10) = '" + tglNow + "') ORDER BY Nama";
        Cursor data = dataBaseAccess.Where("TenagaKerja", whr);

        if (data.getCount() == 0) {
            nomember.setVisibility(View.VISIBLE);
            listAnggota.setVisibility(View.GONE);
        } else {
            Integer[] regno = new Integer[data.getCount()];
            String[] nama = new String[data.getCount()];
            String[] nik = new String[data.getCount()];
            String[] jabatan = new String[data.getCount()];
            int i = 0;
            while (data.moveToNext()) {
                regno[i] = data.getInt(0);
                nama[i] = data.getString(2);
                nik[i] = data.getString(1);
                jabatan[i] = data.getString(3);
                i++;
            }
            listAnggota.setAdapter(new AdapterAnggota(ChooseAnggotaActivity.this, nama, nik, jabatan));
            nomember.setVisibility(View.GONE);
            listAnggota.setVisibility(View.VISIBLE);
            listAnggota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseAnggotaActivity.this);
//                    LayoutInflater inflater = getLayoutInflater();
//                    View row = inflater.inflate(R.layout.dialog_pilih_anggota, null);
//
//                    Button lanjut = row.findViewById(R.id.lanjut);
//                    Button batal = row.findViewById(R.id.batal);
//                    TextView tvNama = row.findViewById(R.id.nama);
//
//                    tvNama.setText(nama[position]);
//
//                    builder.setCancelable(true);
//                    builder.setView(row);
//
//                    AlertDialog alertDialog = builder.create();
//
//                    batal.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                    lanjut.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
                            startActivity(new Intent(ChooseAnggotaActivity.this, AbsensiActivity.class)
                                    .putExtra("Nama", nama[position])
                                    .putExtra("RegNo", regno[position])
                                    .putExtra("KodeAbsen", kodeAbsen));
//                        }
//                    });
//
//                    alertDialog.show();
                }
            });
        }
    }
}