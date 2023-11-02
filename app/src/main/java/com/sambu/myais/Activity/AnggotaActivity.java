package com.sambu.myais.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
//import android.widget.Button;
import android.widget.ListView;
//import android.widget.ProgressBar;
import android.widget.ProgressBar;
import android.widget.TextView;
//import android.widget.Toast;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.ListView.AdapterAnggota;
import com.sambu.myais.R;
import com.sambu.myais.Retrofit.JsonPlaceHolderApi;

import retrofit2.Retrofit;

public class AnggotaActivity extends AppCompatActivity {

//    Button refresh;
    ListView listAnggota;
    ProgressBar ProgressTK;
//    ProgressBar loadingrefresh;
    TextView nomember;

//    Integer IDPancang;

    JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;
    String linkAPI = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota);

//        refresh = findViewById(R.id.refresh);
        listAnggota = findViewById(R.id.listAnggota);
        ProgressTK = findViewById(R.id.ProgressTK);
//        loadingrefresh = findViewById(R.id.loadingrefresh);
        nomember = findViewById(R.id.nomember);

        SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        linkAPI = sharedPreferences.getString("linkAPI", "-");

//        getIDPancang();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                ProgressTK.setVisibility(View.GONE);
                getTenagaKerja();
            }
        }, 500);


//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nomember.setVisibility(View.GONE);
//                listAnggota.setVisibility(View.GONE);
//                refresh.setVisibility(View.GONE);
//                loadingrefresh.setVisibility(View.VISIBLE);
//                getPersonel();
//            }
//        });
    }

//    private void getPersonel() {
//        retrofit = new Retrofit.Builder()
//                .baseUrl(linkAPI)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//
//        Call<List<Personel>> call = jsonPlaceHolderApi.GetPersonel(IDPancang);
//
//        call.enqueue(new Callback<List<Personel>>() {
//            @Override
//            public void onResponse(Call<List<Personel>> call, Response<List<Personel>> response) {
//                if (!response.isSuccessful()) {
//                    Toast.makeText(AnggotaActivity.this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
//                } else {
//                    DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(AnggotaActivity.this.getApplicationContext());
//                    dataBaseAccess.open();
//
//                    dataBaseAccess.DeleteAll("PersonelTable").getCount();
//
//                    List<Personel> personels = response.body();
//
//                    for (Personel getData : personels) {
//                        dataBaseAccess.insertPersonel(getData.getID(),
//                                getData.getIDPersonil(),
//                                getData.getNama(),
//                                getData.getNIK(),
//                                getData.getJabatan(),
//                                getData.getStatus(),
//                                getData.getIM(),
//                                getData.getJamMasuk(),
//                                getData.getJamPulang());
//                    }
//                    Toast.makeText(AnggotaActivity.this, "Berhasil Memperbaharui Anggota", Toast.LENGTH_SHORT).show();
//                    dataBaseAccess.close();
//                }
//                getAnggota();
//            }
//
//            @Override
//            public void onFailure(Call<List<Personel>> call, Throwable t) {
//                getAnggota();
//                Toast.makeText(AnggotaActivity.this, "Koneksi Bermasalah - Gagal Mengurai Data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void getTenagaKerja() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Order("TenagaKerja", "Nama");

        if (data.getCount() == 0) {
            nomember.setVisibility(View.VISIBLE);
            listAnggota.setVisibility(View.GONE);
//            ProgressTK.setVisibility(View.VISIBLE);

        } else {

            String[] Nama = new String[data.getCount()];
            String[] NIK = new String[data.getCount()];
            String[] Jabatan = new String[data.getCount()];
            int i = 0;
            while(data.moveToNext()){
                Nama[i] = data.getString(2);
                NIK[i] = data.getString(1);
                Jabatan[i] = data.getString(3);
                i++;
            }
            listAnggota.setAdapter(new AdapterAnggota(AnggotaActivity.this, Nama, NIK, Jabatan));
            nomember.setVisibility(View.GONE);
            listAnggota.setVisibility(View.VISIBLE);
        }
//        ProgressTK.setVisibility(View.GONE);
//        loadingrefresh.setVisibility(View.GONE);
//        refresh.setVisibility(View.VISIBLE);
    }

//    private void getIDPancang() {
//        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
//        dataBaseAccess.open();
//        Cursor data = dataBaseAccess.Get("PancangTable");
//
//        if (data.getCount() == 0) {
//            startActivity(new Intent(this, SplashScreen.class));
//        } else {
//            while (data.moveToNext()) {
//                IDPancang = data.getInt(0);
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AnggotaActivity.this, MenuActivity.class));
    }
}