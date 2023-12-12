package com.sambu.myais.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.ListView.AdapterHistory;
import com.sambu.myais.ListView.AdapterKecelakaanKerja;
import com.sambu.myais.R;
import com.sambu.myais.SplashScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ListKecelakaanKerjaActivity extends AppCompatActivity {
    String tanggalAPI, UserID = "", GroupAccess = "";

    FloatingActionButton add;
    ListView listViewKecelakaan;
    ProgressBar ProgressTK;
    TextView nomember;

    SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfApiFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kecelakaan_kerja);

        add = findViewById(R.id.add);
        listViewKecelakaan = findViewById(R.id.listKecelakaan);
        ProgressTK = findViewById(R.id.ProgressTK);
        nomember = findViewById(R.id. nomember);

        tanggalAPI = sdfApi.format(new Date());

        ProgressTK.setVisibility(View.VISIBLE);

        getUser();
        getData(tanggalAPI);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ProgressTK.setVisibility(View.GONE);
            }
        }, 250);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListKecelakaanKerjaActivity.this, KecelakaanKerjaActivity.class);
                startActivity(intent);
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

    private void getData(String tanggalAPI) {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(ListKecelakaanKerjaActivity.this);
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Where("vwKecelakaanKerja", "date(Tanggal) = date('" + tanggalAPI + "') OR Terkirim = 0 ORDER BY Nama");

        if (data.getCount() == 0) {
            listViewKecelakaan.setAdapter(null);
            nomember.setVisibility(View.VISIBLE);
        } else {
            nomember.setVisibility(View.GONE);
            Integer[] ID = new Integer[data.getCount()];
            Integer[] RegNo = new Integer[data.getCount()];
            String[] Nama = new String[data.getCount()];
            String[] Tanggal = new String[data.getCount()];
            String[] KodeSubPersil = new String[data.getCount()];
            Integer[] KecelakaanID = new Integer[data.getCount()];
            String[] Kecelakaan = new String[data.getCount()];
            String[] KronologiKecelakaan = new String[data.getCount()];
            Boolean[] Terkirim = new Boolean[data.getCount()];
            String[] TanggalInd = new String[data.getCount()];


            int i = 0;
            while (data.moveToNext()) {
                ID[i] = data.getInt(0);
                RegNo[i] = data.getInt(2);
                Nama[i] = data.getString(3);
                Tanggal[i] = data.getString(1);
                KodeSubPersil[i] = data.getString(4);
                KecelakaanID[i] = data.getInt(5);
                Kecelakaan[i] = data.getString(6);
                KronologiKecelakaan[i] = data.getString(7);
                Terkirim[i] = data.getInt(8) > 0;
                try {
                    TanggalInd[i] = sdf.format(Objects.requireNonNull(sdfApiFull.parse(data.getString(1))));
                } catch (ParseException e) {
                    TanggalInd[i] = data.getString(1);
                }
                i++;
            }
            listViewKecelakaan.setAdapter(new AdapterKecelakaanKerja(ListKecelakaanKerjaActivity.this, ID, RegNo, Nama, Tanggal, KodeSubPersil, KecelakaanID, Kecelakaan, KronologiKecelakaan, Terkirim, TanggalInd));
            listViewKecelakaan.setVisibility(View.VISIBLE);
        }
    }

    public void onBackPressed() {
        if(GroupAccess.equals("Mandor")){
            startActivity(new Intent(ListKecelakaanKerjaActivity.this, MenuActivity.class));
        }else {
            startActivity(new Intent(ListKecelakaanKerjaActivity.this, Menu2Activity.class));
        }
    }
}