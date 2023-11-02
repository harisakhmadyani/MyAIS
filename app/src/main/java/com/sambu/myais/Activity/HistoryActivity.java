package com.sambu.myais.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.R;
import com.sambu.myais.SplashScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    Button tanggal;
    ImageButton notsend;
    ListView listView;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd");

    Integer IDPancang;
    boolean linklokal = false;

    String tanggalAPI;
    String tanggalIndo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tanggal = findViewById(R.id.tanggal);
        notsend = findViewById(R.id.notsend);
        listView = findViewById(R.id.listView);

        SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        linklokal = !sharedPreferences.getString("koneksi", "public").equals("public");

        tanggalAPI = sdfApi.format(new Date());
        tanggalIndo = sdf.format(new Date());

//        getIDPancang();

        tanggal.setText("Tanggal : " + tanggalIndo);
//        getData(tanggalAPI);

        notsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("Not Send");
            }
        });

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            tanggalIndo = sdf.format(Objects.requireNonNull(sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)));
                            tanggalAPI = sdfApi.format(Objects.requireNonNull(sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)));
                            tanggal.setText("Tanggal : " + tanggalIndo);
                            getData(tanggalAPI);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getData(String tanggalAPI){
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(this);
        dataBaseAccess.open();

        Cursor data;

        if(tanggalAPI.equals("Not Send")){
            data = dataBaseAccess.Where("vwAbsensi", "Terkirim = 0");
            tanggal.setText("Absensi Tidak Terkirim");
        } else {
            data = dataBaseAccess.Where("vwAbsensi", "date(CreatedDate) = date('" + tanggalAPI + "')");
        }

        if(data.getCount() == 0){
            Toast.makeText(this, "Tidak Ada Absensi", Toast.LENGTH_SHORT).show();
            listView.setAdapter(null);
        } else {
            Integer[] ID = new Integer[data.getCount()];
            String[] PersonelID = new String[data.getCount()];
            String[] Nama = new String[data.getCount()];
            String[] Kehadiran = new String[data.getCount()];
            String[] ShortName = new String[data.getCount()];
            Double[] Latitude = new Double[data.getCount()];
            Double[] Longitude = new Double[data.getCount()];
            Integer[] IDPancang = new Integer[data.getCount()];
            byte[][] Foto = new byte[data.getCount()][];
            Boolean[] Terkirim = new Boolean[data.getCount()];
            String[] CreatedDate = new String[data.getCount()];
            String[] AppVersion = new String[data.getCount()];
            int i = 0;
            while(data.moveToNext()){
                ID[i] = data.getInt(0);
                PersonelID[i] = data.getString(1);
                Nama[i] = data.getString(2);
                Kehadiran[i] = data.getString(4);
                ShortName[i] = data.getString(5);
                Latitude[i] = data.getDouble(6);
                Longitude[i] = data.getDouble(7);
                IDPancang[i] = data.getInt(8);
                Foto[i] = data.getBlob(10);
                Terkirim[i] = data.getInt(16) > 0;
                CreatedDate[i] = data.getString(17);
                AppVersion[i] = data.getString(18);
                i++;
            }
//            AdapterHistory adapterAbsensi = new AdapterHistory(this, ID, Nama, Foto, Kehadiran, Latitude, Longitude, CreatedDate, Terkirim, PersonelID, IDPancang, ShortName, CreatedDate, AppVersion);
//            listView.setAdapter(adapterAbsensi);
        }
    }

    private void getIDPancang() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Get("PancangTable");
        if (data.getCount() == 0) {
            startActivity(new Intent(this, SplashScreen.class));
        }
        while (data.moveToNext()) {
            IDPancang = data.getInt(0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(HistoryActivity.this, MenuActivity.class));
    }
}