package com.sambu.myais.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class SettingsActivity extends AppCompatActivity {

//    Button btnaddjaringan, btnkehadiran, btnpancang, btnclear,
    LinearLayout btnclear,btnDownloadkehadiran,btnDownloadTenagaKerja,btndownloadsubpersil, btnDownloadTipeKecelakaan;
    LinearLayout logout;
//    ProgressBar loadingaddjaringan, loadingkehadiran, loadingpancang;
    ProgressBar loadingdownloaddata,loadingdownloadsubpersil,loadingdownloadTTkkerja,loadingdownloadkehadiran, loadingDownloadTipeKecelakaan;
    Spinner jenisjaringan, subpersilkelapa, subpersilkelapamasuk;
//    Integer IDPancang;
    String pilihanjaringan = "", pilihansubpersil = "", linkAPI = "", subpersil = "", UserID = "", pilihansubpersilmasuk = "" , subpersilmasuk = "", GroupAccess = "";

    JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;

    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        jenisjaringan = findViewById(R.id.jenisjaringan);
        subpersilkelapa = findViewById(R.id.subpersilkelapa);
        subpersilkelapamasuk = findViewById(R.id.subpersilkelapamasuk);
        btnDownloadkehadiran = findViewById(R.id.btnDownloadkehadiran);
        btnDownloadTenagaKerja = findViewById(R.id.btnDownloadTenagaKerja);
        btndownloadsubpersil = findViewById(R.id.btndownloadsubpersil);
        btnDownloadTipeKecelakaan = findViewById(R.id.btndownloadtipekecelakaan);
        loadingdownloadkehadiran = findViewById(R.id.loadingdownloadkehadiran);
        loadingdownloadTTkkerja = findViewById(R.id.loadingdownloadTTkkerja);
        loadingdownloadsubpersil = findViewById(R.id.loadingdownloadsubpersil);
        loadingDownloadTipeKecelakaan = findViewById(R.id.loadingdownloadtipekecelakaan);
//        btndownloaddata = findViewById(R.id.btndownloaddata);
//        loadingdownloaddata = findViewById(R.id.loadingdownloaddata);
        btnclear = findViewById(R.id.btnclear);
        logout = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        pilihanjaringan = sharedPreferences.getString("pilihanjaringan", "-");
        pilihansubpersil = sharedPreferences.getString("pilihansubpersil", "-");
        pilihansubpersilmasuk = sharedPreferences.getString("pilihansubpersilmasuk", "-");
        linkAPI = sharedPreferences.getString("linkAPI", "-");
        subpersil = sharedPreferences.getString("subpersil", "-");
        subpersilmasuk = sharedPreferences.getString("subpersilmasuk", "-");

        getUser();
        getJenisJaringan();
        getSubPersilMasukLokal();
        getSubPersilKelapaLokal();

//        btnaddjaringan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
//                LayoutInflater inflater = getLayoutInflater();
//                View view = inflater.inflate(R.layout.dialog_add_linkapi, null);
//
//                Button ya = view.findViewById(R.id.ya);
//                Button tidak = view.findViewById(R.id.tidak);
//                EditText apiname = view.findViewById(R.id.apiname);
//                EditText apiurl = view.findViewById(R.id.apiurl);
//
//                builder.setCancelable(true);
//                builder.setView(view);
//
//                AlertDialog alertDialog = builder.create();
//
//                ya.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (apiname.getText().toString().equals("") || apiurl.getText().toString().equals("")) {
//                            Toast.makeText(SettingsActivity.this, "Harap Lengkapi Data Anda", Toast.LENGTH_SHORT).show();
//                        } else {
//                            if (!apiurl.getText().toString().contains("http://")) {
//                                Toast.makeText(SettingsActivity.this, "Maaf Bukan Alamat Url yang Benar", Toast.LENGTH_SHORT).show();
//                            } else if (apiurl.getText().toString().length() < 6) {
//                                Toast.makeText(SettingsActivity.this, "Maaf Bukan Alamat Url yang Benar", Toast.LENGTH_SHORT).show();
//                            } else {
//                                char lastchar = apiurl.getText().toString().charAt(apiurl.getText().toString().length() - 1);
//                                if (lastchar != '/') {
//                                    Toast.makeText(SettingsActivity.this, "Maaf Bukan Alamat Url yang Benar", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    btnaddjaringan.setVisibility(View.GONE);
//                                    loadingaddjaringan.setVisibility(View.VISIBLE);
//                                    cekJaringan(apiname.getText().toString(), apiurl.getText().toString());
//                                    alertDialog.dismiss();
//                                }
//                            }
//                        }
//                    }
//                });
//
//                tidak.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                alertDialog.show();
//            }
//        });
//
//        btnkehadiran.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnkehadiran.setVisibility(View.GONE);
//                loadingkehadiran.setVisibility(View.VISIBLE);
//                getKehadiran();
//            }
//        });

//        btndownloaddata.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btndownloaddata.setVisibility(View.GONE);
//                loadingdownloaddata.setVisibility(View.VISIBLE);
//                getKehadiran();
//                getTenagaKerja();
//                getSubPersilKelapa();
////                btndownloaddata.setVisibility(View.VISIBLE);
////                loadingdownloaddata.setVisibility(View.GONE);
//
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        btndownloaddata.setVisibility(View.VISIBLE);
//                        loadingdownloaddata.setVisibility(View.GONE);
//                    }
//                }, 500);
//            }
//        });

        btnDownloadkehadiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDownloadkehadiran.setVisibility(View.GONE);
                loadingdownloadkehadiran.setVisibility(View.VISIBLE);
                getKehadiran();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnDownloadkehadiran.setVisibility(View.VISIBLE);
                        loadingdownloadkehadiran.setVisibility(View.GONE);

                    }
                },500);

            }
        });

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

        btndownloadsubpersil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btndownloadsubpersil.setVisibility(View.GONE);
                loadingdownloadsubpersil.setVisibility(View.VISIBLE);
                getSubPersilKelapa();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btndownloadsubpersil.setVisibility(View.VISIBLE);
                        loadingdownloadsubpersil.setVisibility(View.GONE);
                        getSubPersilMasukLokal();
                        getSubPersilKelapaLokal();

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


//
//        btnpancang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnpancang.setVisibility(View.GONE);
//                loadingpancang.setVisibility(View.VISIBLE);
//                getLokasi();
//            }
//        });
//
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_clear_history, null);

                Button ya = view.findViewById(R.id.ya);
                Button tidak = view.findViewById(R.id.tidak);
                EditText validasi = view.findViewById(R.id.validasi);

                builder.setCancelable(true);
                builder.setView(view);

                AlertDialog alertDialog = builder.create();

                ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validasi.getText().toString().equals("hapus")) {
                            DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(SettingsActivity.this);
                            dataBaseAccess.open();

                            String query = "SELECT ID FROM Absensi WHERE Terkirim = 0";
                            Cursor data = dataBaseAccess.GetCustom(query);
//                            String query = "SELECT A.ID FROM Absensi AS A INNER JOIN Absensi AS B ON (A.RegNo = B.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(B.Tanggal, 1, 10) AND B.KodeAbsen = 'Absen Pulang') INNER JOIN Absensi AS C ON (A.RegNo = C.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(C.Tanggal, 1, 10) AND C.KodeAbsen = 'Absen Istirahat') INNER JOIN Absensi AS D ON (A.RegNo = D.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(D.Tanggal, 1, 10) AND D.KodeAbsen = 'Absen Masuk 2') WHERE A.KodeAbsen = 'Absen Masuk' AND A.Terkirim = 1";
//                            Cursor data = dataBaseAccess.GetCustom(query);
//
//                            String query2 = "SELECT A.ID FROM Absensi AS A INNER JOIN Absensi AS B ON (A.RegNo = B.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(B.Tanggal, 1, 10) AND B.KodeAbsen = 'Absen Masuk') INNER JOIN Absensi AS C ON (A.RegNo = C.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(C.Tanggal, 1, 10) AND C.KodeAbsen = 'Absen Istirahat') INNER JOIN Absensi AS D ON (A.RegNo = D.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(D.Tanggal, 1, 10) AND D.KodeAbsen = 'Absen Masuk 2') WHERE A.KodeAbsen = 'Absen Pulang' AND A.Terkirim = 1";
//                            Cursor data2 = dataBaseAccess.GetCustom(query2);
//
//                            String query3 = "SELECT A.ID FROM Absensi AS A INNER JOIN Absensi AS B ON (A.RegNo = B.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(B.Tanggal, 1, 10) AND B.KodeAbsen = 'Absen Masuk') INNER JOIN Absensi AS C ON (A.RegNo = C.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(C.Tanggal, 1, 10) AND C.KodeAbsen = 'Absen Pulang') INNER JOIN Absensi AS D ON (A.RegNo = D.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(D.Tanggal, 1, 10) AND D.KodeAbsen = 'Absen Masuk 2') WHERE A.KodeAbsen = 'Absen Istirahat' AND A.Terkirim = 1";
//                            Cursor data3 = dataBaseAccess.GetCustom(query3);
//
//                            String query4 = "SELECT A.ID FROM Absensi AS A INNER JOIN Absensi AS B ON (A.RegNo = B.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(B.Tanggal, 1, 10) AND B.KodeAbsen = 'Absen Masuk') INNER JOIN Absensi AS C ON (A.RegNo = C.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(C.Tanggal, 1, 10) AND C.KodeAbsen = 'Absen Istirahat') INNER JOIN Absensi AS D ON (A.RegNo = D.RegNo AND SUBSTR(A.Tanggal, 1, 10) = SUBSTR(D.Tanggal, 1, 10) AND D.KodeAbsen = 'Absen Pulang') WHERE A.KodeAbsen = 'Absen Masuk 2' AND A.Terkirim = 1";
//                            Cursor data4 = dataBaseAccess.GetCustom(query4);

//                            if (data.getCount() > 0 && data2.getCount() > 0 && data3.getCount() > 0 && data4.getCount() > 0){
//                                while (data.moveToNext()) {
//                                    Integer IDMasuk;
//                                    IDMasuk = data.getInt(0);
//                                    Cursor dlt = dataBaseAccess.DeleteWhere("Absensi", "ID =" + IDMasuk);
//                                    if (dlt.getCount() == 0) {}
//
//                                }
//
//                                while (data2.moveToNext()) {
//                                    Integer IDPulang;
//                                    IDPulang = data2.getInt(0);
//                                    Cursor dlt2 = dataBaseAccess.DeleteWhere("Absensi", "ID =" + IDPulang);
//                                    if (dlt2.getCount() == 0) {}
//                                }
//
//                                while (data3.moveToNext()) {
//                                    Integer IDIstirahat;
//                                    IDIstirahat = data3.getInt(0);
//                                    Cursor dlt3 = dataBaseAccess.DeleteWhere("Absensi", "ID =" + IDIstirahat);
//                                    if (dlt3.getCount() == 0) {}
//                                }
//
//                                while (data4.moveToNext()) {
//                                    Integer IDMasuk2;
//                                    IDMasuk2 = data4.getInt(0);
//                                    Cursor dlt4 = dataBaseAccess.DeleteWhere("Absensi", "ID =" + IDMasuk2);
//                                    if (dlt4.getCount() == 0) {}
//                                }
//                                GetAlertSuccess("History absensi berhasil dihapus");
                            if (data.getCount() > 0){
                                GetAlertFailed("Ada data yeng belum dikirim");
                            }
                            else {
//                                GetAlertFailed("Data tidak ditemukan");
                                dataBaseAccess.DeleteAll("Absensi").getCount();
                                GetAlertSuccess("History absensi berhasil dihapus");
                            }
//                            if (data.getCount() == 0) {
//
//                            } else {
//                                while (data.moveToNext()) {
//                                    Integer IDMasuk;
//                                    IDMasuk = data.getInt(0);
//
//                                    Cursor dlt = dataBaseAccess.DeleteWhere("Absensi", "ID =" + IDMasuk);
//                                }
//                            }
//
//                            if (data2.getCount() == 0) {
//
//                            } else {
//                                while (data2.moveToNext()) {
//                                    Integer IDPulang;
//                                    IDPulang = data.getInt(0);
//
//                                    Cursor dlt2 = dataBaseAccess.DeleteWhere("Absensi", "ID =" + IDPulang);
//                                }
//                            }

//                            Cursor data = dataBaseAccess.DeleteWhere("Absensi", "Terkirim = 1");
//
//                            if (data.getCount() == 0) {
////                                Toast.makeText(SettingsActivity.this, "Riwayat absensi berhasil dihapus", Toast.LENGTH_SHORT).show();
//                                GetAlertSuccess("History absensi berhasil dihapus");
//                            } else {
////                                Toast.makeText(SettingsActivity.this, "Riwayat sbsensi gagal dihapus", Toast.LENGTH_SHORT).show();
//                                GetAlertFailed("History absensi gagal dihapus");
//                            }
                        } else {
//                            Toast.makeText(SettingsActivity.this, "Verifikasi yang anda masukkan salah", Toast.LENGTH_SHORT).show();
                            GetAlertFailed("Verifikasi yang anda masukkan salah");
                        }
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_logout, null);

                Button ya = view.findViewById(R.id.ya);
                Button tidak = view.findViewById(R.id.tidak);
//                EditText verifikasilogout = view.findViewById(R.id.verifikasilogout);

                builder.setCancelable(true);
                builder.setView(view);

                AlertDialog alertDialog = builder.create();

                ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (verifikasilogout.getText().toString().toLowerCase().equals("pancang")) {
//                            logoutPerangkat();
//                        } else {
//                            Toast.makeText(SettingsActivity.this, "Verifikasi Yang Anda Masukkan Salah", Toast.LENGTH_SHORT).show();
//                        }
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
                GroupAccess = data.getString(2);
            }
        }
    }

//    private void cekJaringan(String apiname, String apiurl) {
//        Toast.makeText(this, "Mohon Tunggu Sedang Mengecek Jaringan Anda", Toast.LENGTH_SHORT).show();
//        retrofit = new Retrofit.Builder()
//                .baseUrl(apiurl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//
//        Call<List<Kehadiran>> call = jsonPlaceHolderApi.getKehadiran();
//
//        call.enqueue(new Callback<List<Kehadiran>>() {
//            @Override
//            public void onResponse(Call<List<Kehadiran>> call, Response<List<Kehadiran>> response) {
//                btnaddjaringan.setVisibility(View.VISIBLE);
//                loadingaddjaringan.setVisibility(View.GONE);
//                if(!response.isSuccessful()){
//                    Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah/Url Tidak Benar", Toast.LENGTH_SHORT).show();
//                } else {
//                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(SettingsActivity.this.getApplicationContext());
//                    dataBaseAccess.open();
//
//                    Cursor data = dataBaseAccess.Where("APIUrl", "APIUrl = '" + apiurl + "'");
//
//                    if (data.getCount() == 0) {
//                        if (dataBaseAccess.insertUrl(apiname, apiurl)) {
//                            pilihanjaringan = apiname;
//                            linkAPI = apiurl;
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("pilihanjaringan", pilihanjaringan);
//                            editor.putString("linkAPI", linkAPI);
//                            editor.apply();
//                            Toast.makeText(SettingsActivity.this, "URL Bisa DiGunakan", Toast.LENGTH_SHORT).show();
//                            startActivity(getIntent());
//                        } else {
//                            Toast.makeText(SettingsActivity.this, "Gagal Simpan Data", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("pilihanjaringan", apiname);
//                        editor.putString("linkAPI", apiurl);
//                        editor.apply();
//                        Toast.makeText(SettingsActivity.this, "URL Bisa DiGunakan", Toast.LENGTH_SHORT).show();
//                        startActivity(getIntent());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Kehadiran>> call, Throwable t) {
//                btnaddjaringan.setVisibility(View.VISIBLE);
//                loadingaddjaringan.setVisibility(View.GONE);
//                Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah - Gagal Mengurai Data/Url Tidak Benar", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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

    private void getSubPersilMasukLokal() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(this);
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Where("SubPersilKelapa", "LokasiAbsensiMasuk = 1 ORDER BY KodeSubPersil ASC");

        if(data.getCount() == 0){

        } else {
            String[] aSubPersilMasuk = new String[data.getCount() + 1];
            aSubPersilMasuk[0] = "- Pilih -";

            int i = 1;
            while (data.moveToNext()){
                aSubPersilMasuk[i] = data.getString(0);
                i++;
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.custom_spinner, aSubPersilMasuk);
            subpersilkelapamasuk.setAdapter(arrayAdapter);
            if(!pilihansubpersilmasuk.equals("-")) {
                subpersilkelapamasuk.setSelection(arrayAdapter.getPosition(pilihansubpersilmasuk));
            }
            subpersilkelapamasuk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pilihansubpersilmasuk", aSubPersilMasuk[position]);
                    editor.putString("subpersilmasuk", aSubPersilMasuk[position]);
                    editor.apply();
                    pilihansubpersilmasuk = aSubPersilMasuk[position];
                    subpersilmasuk = aSubPersilMasuk[position];

                    if (position > 0){
                        DataBaseAccess dbaAccess = DataBaseAccess.getInstance(SettingsActivity.this);
                        dbaAccess.open();
                        dbaAccess.inactiveSubPersilMasuk();
                        dbaAccess.activeSubPersilMasuk(subpersilmasuk);
                        GetAlertSuccess("Persil berhasil di set");
                        dbaAccess.close();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        dataBaseAccess.close();
    }

    private void getSubPersilKelapaLokal() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(this);
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Order("SubPersilKelapa", "KodeSubPersil ASC");

        if(data.getCount() == 0){

        } else {
            String[] aSubPersil = new String[data.getCount() + 1];
            aSubPersil[0] = "- Pilih -";

            int i = 1;
            while (data.moveToNext()){
                aSubPersil[i] = data.getString(0);
                i++;
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.custom_spinner, aSubPersil);
            subpersilkelapa.setAdapter(arrayAdapter);
            if(!pilihansubpersil.equals("-")) {
                subpersilkelapa.setSelection(arrayAdapter.getPosition(pilihansubpersil));
            }
            subpersilkelapa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pilihansubpersil", aSubPersil[position]);
                    editor.putString("subpersil", aSubPersil[position]);
                    editor.apply();
                    pilihansubpersil = aSubPersil[position];
                    subpersil = aSubPersil[position];

                    if (position > 0){
                        DataBaseAccess dbaAccess = DataBaseAccess.getInstance(SettingsActivity.this);
                        dbaAccess.open();
                        dbaAccess.inactiveSubPersilKelapa();
                        dbaAccess.activeSubPersilKelapa(subpersil);
                        GetAlertSuccess("Persil berhasil di set");
                        dbaAccess.close();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        dataBaseAccess.close();
    }

    private void getKehadiran() {
        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Kehadiran>> call = jsonPlaceHolderApi.GetKehadiran();

        call.enqueue(new Callback<List<Kehadiran>>() {
            @Override
            public void onResponse(Call<List<Kehadiran>> call, Response<List<Kehadiran>> response) {
                if (!response.isSuccessful()) {
                    GetAlertFailed("Koneksi Bermasalah");
//                    Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(SettingsActivity.this);
                    dataBaseAccess.open();

                    dataBaseAccess.DeleteAll("Kehadiran").getCount();

                    List<Kehadiran> kh = response.body();

                    if (kh.get(0).GetStatus().equals("failed")){
                        GetAlertFailed("Data kehadiran tidak ditemukan");
                    }
                    else {
                        for (Kehadiran dt : kh) {
                            dataBaseAccess.insertKehadiran(dt.getID(), dt.getKeterangan(), dt.getKehadiran());
                        }
                        GetAlertSuccess("Kehadiran berhasil diperbaharui");
//                    Toast.makeText(SettingsActivity.this, "Kehadiran berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                    }
                    dataBaseAccess.close();

                }
            }

            @Override
            public void onFailure(Call<List<Kehadiran>> call, Throwable t) {
                GetAlertFailed("Koneksi Bermasalah - Gagal Mengurai Data");
//                Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah - Gagal Mengurai Data", Toast.LENGTH_SHORT).show();
            }
        });
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
                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(SettingsActivity.this);
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

        Call<List<TenagaKerja>> call = jsonPlaceHolderApi.GetTKByUser(UserID);

        call.enqueue(new Callback<List<TenagaKerja>>() {
            @Override
            public void onResponse(Call<List<TenagaKerja>> call, Response<List<TenagaKerja>> response) {
                if (!response.isSuccessful()) {
                    GetAlertFailed("Koneksi Bermasalah");
//                    Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(SettingsActivity.this);
                    dataBaseAccess.open();

                    dataBaseAccess.DeleteAll("TenagaKerja").getCount();

                    List<TenagaKerja> tk = response.body();


                    if (tk.get(0).getStatus().equals("failed")){
                        GetAlertFailed("Data tidak ditemukan");
                    }
                        else {
                        for (TenagaKerja dt : tk) {
                            dataBaseAccess.insertTenagaKerja(dt.getRegNo(), dt.getNIK(), dt.getNama(), dt.getKodeWilayah());
                        }
                        GetAlertSuccess("Tenaga kerja berhasil diperbaharui");
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

    private void getSubPersilKelapa() {
        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<SubPersilKelapa>> call = jsonPlaceHolderApi.GetSubPersilByUser(UserID);

        call.enqueue(new Callback<List<SubPersilKelapa>>() {
            @Override
            public void onResponse(Call<List<SubPersilKelapa>> call, Response<List<SubPersilKelapa>> response) {
                if (!response.isSuccessful()) {
                    GetAlertFailed("Koneksi Bermasalah");
//                    Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(SettingsActivity.this);
                    dataBaseAccess.open();

                    dataBaseAccess.DeleteAll("SubPersilKelapa").getCount();

                    List<SubPersilKelapa> spk = response.body();

                    if (spk.get(0).getStatus().equals("failed")){
                        GetAlertFailed("Data tidak ditemukan");
                    }
                    else{
                        for (SubPersilKelapa dt : spk) {
                            dataBaseAccess.insertSubPersilKelapa(dt.getKodeSubPersil(), dt.getLatitude(), dt.getLongitude(), dt.getToleransi(), dt.getLokasiAbsensiMasuk());
                        }
                        GetAlertSuccess("Persil berhasil diperbaharui");
//                    Toast.makeText(SettingsActivity.this, "Sub persil kelapa berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                    }
                    dataBaseAccess.close();
                }
            }

            @Override
            public void onFailure(Call<List<SubPersilKelapa>> call, Throwable t) {
                GetAlertFailed("Koneksi Bermasalah - Gagal Mengurai Data");
//                Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah - Gagal Mengurai Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutPerangkat() {
        Toast.makeText(SettingsActivity.this, "Mohon tunggu sebentar", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(SettingsActivity.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(SettingsActivity.this);
                    dataBaseAccess.open();

                    dataBaseAccess.DeleteAll("SubPersilKelapa").getCount();
                    dataBaseAccess.DeleteAll("TenagaKerja").getCount();
                    dataBaseAccess.DeleteAll("Kehadiran").getCount();
                    dataBaseAccess.DeleteAll("User").getCount();
                    dataBaseAccess.DeleteAll("KecelakaanKerja").getCount();
                    startActivity(new Intent(SettingsActivity.this, SplashScreen.class));
                }
            }

            @Override
            public void onFailure(Call<List<DeviceInfo>> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, "Koneksi Bermasalah - Gagal Mengurai Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    alert dialog success
    private void GetAlertSuccess(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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
//        if(GroupAccess.equals("Mandor")){
            startActivity(new Intent(SettingsActivity.this, MenuActivity.class));
//        }else {
//            startActivity(new Intent(SettingsActivity.this, Menu2Activity.class));
//        }
    }
}