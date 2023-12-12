package com.sambu.myais.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.ListView.AdapterCekAbsensi;
import com.sambu.myais.ListView.AdapterTest;
import com.sambu.myais.R;
import com.sambu.myais.Retrofit.HistoryCekAbsensi;
import com.sambu.myais.Retrofit.JsonPlaceHolderApi;
import com.sambu.myais.SplashScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CekAbsensiActivity extends AppCompatActivity {

    AdapterCekAbsensi adapterCekAbsensi;
    AdapterTest adapterTest;
    Button tanggal;
    ListView listView;
    ProgressBar ProgressTK;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat sdfJam = new SimpleDateFormat("hh:mm");
    SimpleDateFormat sdfApiFull = new SimpleDateFormat("yyyy-MM-dd H:m:s.S");

    String UserID;

    String tanggalAPI, tanggalIndo, linkAPI = "";

    JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;

    Spinner test;
    TextView nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_absensi);

        tanggal = findViewById(R.id.tanggal);
        listView = findViewById(R.id.listView);
        ProgressTK = findViewById(R.id.ProgressTK);
//        test = findViewById(R.id.test);
//        nama = findViewById(R.id.nama);

        SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        linkAPI = sharedPreferences.getString("linkAPI", "-");

        tanggalAPI = sdfApi.format(new Date());
        tanggalIndo = sdf.format(new Date());

        getUserID();

        tanggal.setText("- Pilih Tanggal -");
//        getData(tanggalAPI);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                ProgressTK.setVisibility(View.GONE);
//                getData(tanggalAPI);
//            }
//        }, 500);

//        getData("26-09-2023");
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CekAbsensiActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            ProgressTK.setVisibility(View.VISIBLE);
                            tanggalIndo = sdf.format(Objects.requireNonNull(sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)));
                            tanggalAPI = sdfApi.format(Objects.requireNonNull(sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)));
                            tanggal.setText(tanggalIndo);
                            getData(tanggalAPI);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    ProgressTK.setVisibility(View.GONE);
                                }
                            }, 1000);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    void getData(String tanggal) {
        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        Call<List<HistoryCekAbsensi>> call = jsonPlaceHolderApi.cekAbsensi(UserID, tanggal);

        call.enqueue(new Callback<List<HistoryCekAbsensi>>() {
            @Override
            public void onResponse(Call<List<HistoryCekAbsensi>> call, Response<List<HistoryCekAbsensi>> response) {
                listView.setAdapter(null);
                if (!response.isSuccessful()) {
                    Toast.makeText(CekAbsensiActivity.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    List<HistoryCekAbsensi> absensis = response.body();
                    if (absensis.get(0).getResponse().equals("success")) {
                        String[] Nama = new String[absensis.size()];
                        String[] Nik = new String[absensis.size()];
                        String[] KodeWilayah = new String[absensis.size()];
                        String[] JamMasuk = new String[absensis.size()];
                        String[] JamPulang = new String[absensis.size()];
                        String[] KehadiranMasuk = new String[absensis.size()];
                        String[] KehadiranPulang = new String[absensis.size()];
                        String[] KodeSubPersilMasuk = new String[absensis.size()];
                        String[] KodeSubPersilPulang = new String[absensis.size()];
                        String[] KetMasuk = new String[absensis.size()];
                        String[] KetPulang = new String[absensis.size()];
                        int i = 0;
                        for (HistoryCekAbsensi getData : absensis) {
                            Nama[i] = getData.getNama();
                            Nik[i] = getData.getNik();
                            KodeWilayah[i] = getData.getKodeWilayah();
                            JamMasuk[i] =getData.getJamMasuk();
                            JamPulang[i] =getData.getJamPulang();
                            KehadiranMasuk[i] = getData.getKehadiranMasuk();
                            KehadiranPulang[i] = getData.getKehadiranPulang();
                            KodeSubPersilMasuk[i] = getData.getKodeSubPersilMasuk();
                            KodeSubPersilPulang[i] = getData.getKodeSubPersilPulang();
                            KetMasuk[i] = getData.getKetMasuk();
                            KetPulang[i] = getData.getKetPulang();
//                            try {
//                                TanggalMasuk[i] = sdfJam.format(Objects.requireNonNull(sdfApiFull.parse(getData.getTanggalMasuk())));
//                            } catch (ParseException e) {
//                                TanggalMasuk[i] = getData.getTanggalMasuk();
//                            }
//                            KetAbsen[i] = getData.getKetAbsen();
                            i++;
                        }
                        adapterCekAbsensi = new AdapterCekAbsensi(CekAbsensiActivity.this, Nama,Nik,KodeWilayah,JamMasuk,JamPulang,KehadiranMasuk,KehadiranPulang, KodeSubPersilMasuk, KodeSubPersilPulang, KetMasuk, KetPulang);
                        listView.setAdapter(adapterCekAbsensi);
                    } else {
                        Toast.makeText(CekAbsensiActivity.this, "Tidak ada absensi di tanggal ini", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HistoryCekAbsensi>> call, Throwable t) {
                listView.setAdapter(null);
                Toast.makeText(CekAbsensiActivity.this, "Koneksi bermasalah - gagal mengurai data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void test(String tanggal){
        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<HistoryCekAbsensi>> call = jsonPlaceHolderApi.cekAbsensi(UserID, tanggal);

        call.enqueue(new Callback<List<HistoryCekAbsensi>>() {
            @Override
            public void onResponse(Call<List<HistoryCekAbsensi>> call, Response<List<HistoryCekAbsensi>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CekAbsensiActivity.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    List<HistoryCekAbsensi> absensis = response.body();
                    if (absensis.get(0).getResponse().equals("success")) {
                        String[] aNama = new String[absensis.size() + 1];
                        String[] aNik = new String[absensis.size() + 1];
                        aNik[0] = "pilih";
                        aNama[0] = "-";
                        int i = 1;
                        for (HistoryCekAbsensi getData : absensis) {
                            aNama[i] = getData.getNama();
                            aNik[i] = getData.getNik();
                            i++;
                        }
                        adapterTest= new AdapterTest(CekAbsensiActivity.this, aNama, aNik);
                        test.setAdapter(adapterTest);
                        test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                nama.setText(aNama[position]);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        Toast.makeText(CekAbsensiActivity.this, "Tidak ada absensi di tanggal ini", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HistoryCekAbsensi>> call, Throwable t) {
                listView.setAdapter(null);
                Toast.makeText(CekAbsensiActivity.this, "Koneksi bermasalah - gagal mengurai data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getJenisJaringan() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(this);
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Get("APIUrl");

        if (data.getCount() == 0) {
            Toast.makeText(this, "API Url tidak tersimpan", Toast.LENGTH_SHORT).show();
        } else {
//            String[] aNama = new String[data.getCount() + 1];
//            String[] aLinkAPI = new String[data.getCount() + 1];
            String[] aNama = new String[data.getCount()];
            String[] aLinkAPI = new String[data.getCount()];
//            aNama[0] = "- Pilih -";
//            aLinkAPI[0] = "-";
            int i = 0;
            while (data.moveToNext()) {
                aNama[i] = data.getString(1);
                aLinkAPI[i] = data.getString(2);
                i++;
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.custom_spinner_2, aNama);
            test.setAdapter(arrayAdapter);
//            test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    //if (position != 0) {
//                    SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("pilihanjaringan", aNama[position]);
//                    editor.putString("linkAPI", aLinkAPI[position]);
//                    editor.apply();
//                    APIUrl = aLinkAPI[position];
////                        getPancang();
//                    //}
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
        }
    }

//    private void getIDPancang() {
//        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
//        dataBaseAccess.open();
//        Cursor data = dataBaseAccess.Get("PancangTable");
//        if (data.getCount() == 0) {
//            startActivity(new Intent(this, SplashScreen.class));
//        }
//        while (data.moveToNext()) {
//            IDPancang = data.getInt(0);
//        }
//    }

    private  void  getUserID(){
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Get("User");
        if (data.getCount()==0){
            startActivity(new Intent(this,SplashScreen.class));
        }
        while (data.moveToNext()){
            UserID = data.getString(0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CekAbsensiActivity.this, MenuActivity.class));
    }
}