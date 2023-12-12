package com.sambu.myais.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.ListView.AdapterCekAbsensi;
import com.sambu.myais.ListView.AdapterMonKecelakaan;
import com.sambu.myais.R;
import com.sambu.myais.Retrofit.HistoryCekAbsensi;
import com.sambu.myais.Retrofit.JsonPlaceHolderApi;
import com.sambu.myais.Retrofit.Kecelakaan;
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

public class MonKecelakaanKerjaActivity extends AppCompatActivity {

    LinearLayout btnSearch;
    ProgressBar ProgressTK;

    EditText tgl1, tgl2;
    TextView nomember;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat sdfFull = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfApiFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    AdapterMonKecelakaan adapterMonKecelakaanKerja;

    String tanggalAPI1, tanggalIndo1, tanggalAPI2, tanggalIndo2, linkAPI = "", UserID = "", GroupAccess = "";

    Retrofit retrofit;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_kecelakaan_kerja);

        btnSearch = findViewById(R.id.btnSearch);
        ProgressTK = findViewById(R.id.ProgressTK);
        listView = findViewById(R.id.listView);
        nomember = findViewById(R.id.nomember);
        tgl1 = findViewById(R.id.tgl1);
        tgl2 = findViewById(R.id.tgl2);

        tanggalIndo1 = sdf.format(new Date());
        tanggalAPI1 = sdfApi.format(new Date());
        tanggalIndo2 = sdf.format(new Date());
        tanggalAPI2 = sdfApi.format(new Date());
        tgl1.setText(tanggalIndo1);
        tgl2.setText(tanggalIndo2);

        SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        linkAPI = sharedPreferences.getString("linkAPI", "-");

        getUser();
        getData(tanggalAPI1, tanggalAPI2);


        tgl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MonKecelakaanKerjaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
//                            ProgressTK.setVisibility(View.VISIBLE);
                            tanggalIndo1 = sdf.format(Objects.requireNonNull(sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)));
                            tanggalAPI1 = sdfApi.format(Objects.requireNonNull(sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)));
                            tgl1.setText(tanggalIndo1);
//                            getData(tanggalAPI);
//                            new Handler().postDelayed(new Runnable() {
//                                public void run() {
//                                    ProgressTK.setVisibility(View.GONE);
//                                }
//                            }, 1000);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        tgl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MonKecelakaanKerjaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
//                            ProgressTK.setVisibility(View.VISIBLE);
                            tanggalIndo2 = sdf.format(Objects.requireNonNull(sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)));
                            tanggalAPI2 = sdfApi.format(Objects.requireNonNull(sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)));
                            tgl2.setText(tanggalIndo2);
//                            getData(tanggalAPI);
//                            new Handler().postDelayed(new Runnable() {
//                                public void run() {
//                                    ProgressTK.setVisibility(View.GONE);
//                                }
//                            }, 1000);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(tanggalAPI1, tanggalAPI2);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ProgressTK.setVisibility(View.GONE);
                    }
                }, 1000);
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
                UserID = data.getString(0);
                GroupAccess = data.getString(2);
            }
        }
    }

    private void getData(String tanggal1, String tanggal2) {
        ProgressTK.setVisibility(View.VISIBLE);
        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        Call<List<Kecelakaan>> call = jsonPlaceHolderApi.GetMonKecelakaanKerja(tanggal1, tanggal2);

        call.enqueue(new Callback<List<Kecelakaan>>() {
            @Override
            public void onResponse(Call<List<Kecelakaan>> call, Response<List<Kecelakaan>> response) {
                listView.setAdapter(null);
                if (!response.isSuccessful()) {
                    Toast.makeText(MonKecelakaanKerjaActivity.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    List<Kecelakaan> kec = response.body();
                    if (kec.get(0).getResponse().equals("success")) {
                        String[] Nama = new String[kec.size()];
                        String[] NIK = new String[kec.size()];
                        String[] KodeWilayahTK = new String[kec.size()];
                        String[] Waktu = new String[kec.size()];
                        String[] KodeSubPersil = new String[kec.size()];
                        String[] TipeKecelakaan = new String[kec.size()];
                        String[] Kronologi = new String[kec.size()];
                        String[] TanggalInd = new String[kec.size()];
                        Integer[] StatusVerification = new Integer[kec.size()];

                        int i = 0;
                        for (Kecelakaan getData : kec) {
                            Nama[i] = getData.getNama();
                            NIK[i] = getData.getNIK();
                            KodeWilayahTK[i] = getData.getKodeWilayahTK();
                            Waktu[i] =getData.getWaktu();
                            KodeSubPersil[i] =getData.getKodeSubPersil();
                            TipeKecelakaan[i] = getData.getTipeKecelakaan();
                            Kronologi[i] = getData.getKronologi();
                            try {
                                TanggalInd[i] = sdfFull.format(Objects.requireNonNull(sdfApiFull.parse(getData.getWaktu())));
                            } catch (ParseException e) {
                                TanggalInd[i] = getData.getWaktu();
                            }
                            StatusVerification[i] = getData.getStatusVerification();
                            i++;
                        }
                        adapterMonKecelakaanKerja = new AdapterMonKecelakaan(MonKecelakaanKerjaActivity.this, Nama, NIK, KodeWilayahTK, Waktu, KodeSubPersil, TipeKecelakaan, Kronologi, TanggalInd, StatusVerification);
                        listView.setAdapter(adapterMonKecelakaanKerja);
                    } else {
                        nomember.setVisibility(View.VISIBLE);
                        Toast.makeText(MonKecelakaanKerjaActivity.this, "Tidak ada kecelakaan kerja di tanggal ini", Toast.LENGTH_SHORT).show();
                    }
                }
                btnSearch.setVisibility(View.VISIBLE);
                ProgressTK.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Kecelakaan>> call, Throwable t) {
                listView.setAdapter(null);
                Toast.makeText(MonKecelakaanKerjaActivity.this, "Koneksi bermasalah - gagal mengurai data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        if(GroupAccess.equals("Mandor")){
            startActivity(new Intent(MonKecelakaanKerjaActivity.this, MenuActivity.class));
        }else {
            startActivity(new Intent(MonKecelakaanKerjaActivity.this, Menu2Activity.class));
        }
    }
}