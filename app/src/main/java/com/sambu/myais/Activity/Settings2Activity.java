package com.sambu.myais.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.R;
import com.sambu.myais.Retrofit.DeviceInfo;
import com.sambu.myais.Retrofit.JsonPlaceHolderApi;
import com.sambu.myais.Retrofit.Kehadiran;
import com.sambu.myais.Retrofit.SubPersilKelapa;
import com.sambu.myais.Retrofit.TenagaKerja;
import com.sambu.myais.Retrofit.TipeKecelakaan;
import com.sambu.myais.SplashScreen;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Settings2Activity extends AppCompatActivity {

    LinearLayout btnDownloadTenagaKerja, btnDownloadTipeKecelakaan;
    LinearLayout logout;

    ProgressBar loadingdownloadTTkkerja, loadingDownloadTipeKecelakaan;
    Spinner jenisjaringan;

    String pilihanjaringan = "", linkAPI = "", UserID = "", GroupAccess = "", KodeWilayah = "";

    JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;

    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        jenisjaringan = findViewById(R.id.jenisjaringan);
        btnDownloadTenagaKerja = findViewById(R.id.btnDownloadTenagaKerja);
        btnDownloadTipeKecelakaan = findViewById(R.id.btndownloadtipekecelakaan);
        loadingdownloadTTkkerja = findViewById(R.id.loadingdownloadTTkkerja);
        loadingDownloadTipeKecelakaan = findViewById(R.id.loadingdownloadtipekecelakaan);
        logout = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        pilihanjaringan = sharedPreferences.getString("pilihanjaringan", "-");
        linkAPI = sharedPreferences.getString("linkAPI", "-");

        getUser();
        getJenisJaringan();

        btnDownloadTenagaKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDownloadTenagaKerja.setVisibility(View.GONE);
                loadingdownloadTTkkerja.setVisibility(View.VISIBLE);
                getTenagaKerja();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnDownloadTenagaKerja.setVisibility(View.VISIBLE);
                        loadingdownloadTTkkerja.setVisibility(View.GONE);

                    }
                },500);

            }
        });

        btnDownloadTipeKecelakaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDownloadTipeKecelakaan.setVisibility(View.GONE);
                loadingDownloadTipeKecelakaan.setVisibility(View.VISIBLE);
                getTipeKecelakaan();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnDownloadTipeKecelakaan.setVisibility(View.VISIBLE);
                        loadingDownloadTipeKecelakaan.setVisibility(View.GONE);

                    }
                },500);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings2Activity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_logout, null);

                Button ya = view.findViewById(R.id.ya);
                Button tidak = view.findViewById(R.id.tidak);

                builder.setCancelable(true);
                builder.setView(view);

                AlertDialog alertDialog = builder.create();

                ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutPerangkat();
                        alertDialog.dismiss();
                    }
                });

                tidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
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
                KodeWilayah = data.getString(1);
                GroupAccess = data.getString(2);
            }
        }
    }

    private void getJenisJaringan() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(this);
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Get("APIUrl");

        if(data.getCount() == 0){
            Toast.makeText(this, "API Url Tidak Tersimpan", Toast.LENGTH_SHORT).show();
        } else {
            String[] aNama = new String[data.getCount()];
            String[] aLinkAPI = new String[data.getCount()];
            int i = 0;
            while (data.moveToNext()){
                aNama[i] = data.getString(1);
                aLinkAPI[i] = data.getString(2);
                i++;
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.custom_spinner, aNama);
            jenisjaringan.setAdapter(arrayAdapter);
            if(!pilihanjaringan.equals("-")) {
                jenisjaringan.setSelection(arrayAdapter.getPosition(pilihanjaringan));
            }
            jenisjaringan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pilihanjaringan", aNama[position]);
                    editor.putString("linkAPI", aLinkAPI[position]);
                    editor.apply();
                    pilihanjaringan = aNama[position];
                    linkAPI = aLinkAPI[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        dataBaseAccess.close();
    }

    private void getTipeKecelakaan() {
        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<TipeKecelakaan>> call = jsonPlaceHolderApi.GetTipeKecelakaan();

        call.enqueue(new Callback<List<TipeKecelakaan>>() {
            @Override
            public void onResponse(Call<List<TipeKecelakaan>> call, Response<List<TipeKecelakaan>> response) {
                if (!response.isSuccessful()) {
                    GetAlertFailed("Koneksi Bermasalah");
//                    Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(Settings2Activity.this);
                    dataBaseAccess.open();

                    dataBaseAccess.DeleteAll("TipeKecelakaan").getCount();

                    List<TipeKecelakaan> tp = response.body();

                    if (tp.get(0).GetStatus().equals("failed")){
                        GetAlertFailed("Data tipe kecelakaan tidak ditemukan");
                    }
                    else {
                        for (TipeKecelakaan dt : tp) {
                            dataBaseAccess.insertTipeKecelakaan(dt.getKecelakaanID(), dt.getKecelakaan(), dt.getKeterangan(), dt.getSort());
                        }
                        GetAlertSuccess("Tipe kecelakaan berhasil diperbaharui");
//                    Toast.makeText(SettingsActivity.this, "Kehadiran berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                    }
                    dataBaseAccess.close();

                }
            }

            @Override
            public void onFailure(Call<List<TipeKecelakaan>> call, Throwable t) {
                GetAlertFailed("Koneksi Bermasalah - Gagal Mengurai Data");
//                Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah - Gagal Mengurai Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTenagaKerja() {
        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<TenagaKerja>> call = jsonPlaceHolderApi.GetTKByDept(KodeWilayah);

        call.enqueue(new Callback<List<TenagaKerja>>() {
            @Override
            public void onResponse(Call<List<TenagaKerja>> call, Response<List<TenagaKerja>> response) {
                if (!response.isSuccessful()) {
                    GetAlertFailed("Koneksi Bermasalah");
//                    Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(Settings2Activity.this);
                    dataBaseAccess.open();

                    dataBaseAccess.DeleteAll("TenagaKerja").getCount();

                    List<TenagaKerja> tk = response.body();


                    if (tk.get(0).getStatus().equals("failed")){
                        GetAlertFailed("Data tidak ditemukan");
                    } else {
                        for (TenagaKerja dt : tk) {
                            dataBaseAccess.insertTenagaKerja(dt.getRegNo(), dt.getNIK(), dt.getNama(), dt.getKodeWilayah());
                        }
                        GetAlertSuccess("Karyawan berhasil diperbaharui");
//                    Toast.makeText(SettingsActivity.this, "Tenaga kerja berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                    }
                    dataBaseAccess.close();

                }
            }

            @Override
            public void onFailure(Call<List<TenagaKerja>> call, Throwable t) {
                GetAlertFailed("Koneksi Bermasalah - Gagal Mengurai Data");

//                Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah - Gagal Mengurai Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutPerangkat() {
        Toast.makeText(Settings2Activity.this, "Mohon tunggu sebentar", Toast.LENGTH_SHORT).show();

        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<DeviceInfo>> call = jsonPlaceHolderApi.Logout(UserID);

        call.enqueue(new Callback<List<DeviceInfo>>() {
            @Override
            public void onResponse(Call<List<DeviceInfo>> call, Response<List<DeviceInfo>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Settings2Activity.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(Settings2Activity.this);
                    dataBaseAccess.open();

                    dataBaseAccess.DeleteAll("SubPersilKelapa").getCount();
                    dataBaseAccess.DeleteAll("TenagaKerja").getCount();
                    dataBaseAccess.DeleteAll("Kehadiran").getCount();
                    dataBaseAccess.DeleteAll("User").getCount();
                    dataBaseAccess.DeleteAll("KecelakaanKerja").getCount();
                    startActivity(new Intent(Settings2Activity.this, SplashScreen.class));
                }
            }

            @Override
            public void onFailure(Call<List<DeviceInfo>> call, Throwable t) {
                Toast.makeText(Settings2Activity.this, "Koneksi Bermasalah - Gagal Mengurai Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    alert dialog success
    private void GetAlertSuccess(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings2Activity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.succes_dialog,null);
        Button btnSuccess = view.findViewById(R.id.btnSuccess);
        TextView Txt = view.findViewById(R.id.TxtSuccess);
        builder.setCancelable(true);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        Txt.setText(msg);
        alertDialog.show();
    }


//    alert dialog failed
    private void GetAlertFailed(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings2Activity.this);
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
        startActivity(new Intent(Settings2Activity.this, Menu2Activity.class));
    }
}