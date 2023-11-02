package com.sambu.myais.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.ListView;

import com.sambu.myais.Activity.ui.main.SectionsPagerAdapter;
import com.sambu.myais.R;
import com.sambu.myais.databinding.ActivityMainHistoryNewBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainHistoryNewActivity extends AppCompatActivity {

    Button tanggal;
    ListView listView;


    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd");

    boolean linklokal = false;
    String tanggalAPI;
    String tanggalIndo;

    private ActivityMainHistoryNewBinding binding;

    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_history_new);

        SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        linklokal = !sharedPreferences.getString("koneksi", "public").equals("public");
//
        tanggalAPI = sdfApi.format(new Date());
        tanggalIndo = sdf.format(new Date());
//
////        tanggal.setText("Tanggal : " + tanggalIndo);
//        getData(tanggalAPI);
//
//
        binding = ActivityMainHistoryNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }
}

//    @SuppressLint("SetTextI18n")
//    private void getData(String tanggalAPI) {
//        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(this);
//        dataBaseAccess.open();
//
//        Cursor data;
//
//        if (tanggalAPI.equals("Not Send")) {
//            data = dataBaseAccess.Where("vwAbsensiNew", "Terkirim = 0");
//            tanggal.setText("Absensi Tidak Terkirim");
//        } else {
//            data = dataBaseAccess.Where("vwAbsensiNew", "date(Tanggal) = date('" + tanggalAPI + "')");
//        }
//
//        if (data.getCount() == 0) {
//            Toast.makeText(this, "Tidak Ada Absensi", Toast.LENGTH_SHORT).show();
//            listView.setAdapter(null);
//        } else {
//            Integer[] ID = new Integer[data.getCount()];
//            Integer[] RegNo = new Integer[data.getCount()];
//            String[] NIK = new  String[data.getCount()];
//            String[] Nama = new String[data.getCount()];
//            String[] KodeWilayah = new  String[data.getCount()];
//            String[] Kehadiran = new String[data.getCount()];
//            String[] KodeAbsen = new String[data.getCount()];
//            String[] Tanggal = new String[data.getCount()];
//            String[] KodeSubPersil = new String[data.getCount()];
//            Double[] Latitude = new Double[data.getCount()];
//            Double[] Longitude = new Double[data.getCount()];
//            byte[][] Foto = new byte[data.getCount()][];
//            Boolean[] Terkirim = new Boolean[data.getCount()];
//            String[] SerialDevice = new String[data.getCount()];
//            String[] AppVersion = new String[data.getCount()];
//            int i = 0;
//            while (data.moveToNext()) {
//                ID[i] = data.getInt(0);
//                RegNo[i] = data.getInt(1);
//                NIK[i] = data.getString(2);
//                Nama[i] = data.getString(3);
//                KodeWilayah[i] = data.getString(4);
//                Kehadiran[i] = data.getString(5);
//                KodeAbsen[i] = data.getString(7);
//                Tanggal[i] = data.getString(8);
//                KodeSubPersil[i] = data.getString(9);
//                Latitude[i] = data.getDouble(10);
//                Longitude[i] = data.getDouble(11);
//                Foto[i] = data.getBlob(12);
//                Terkirim[i] = data.getInt(13) > 0;
//                SerialDevice[i] = data.getString(14);
//                AppVersion[i] = data.getString(15);
//                i++;
//            }
//            AdapterHistory adapterAbsensi = new AdapterHistory(this, ID, RegNo, NIK, Nama, KodeWilayah, Kehadiran, KodeAbsen, Tanggal, KodeSubPersil, Latitude, Longitude, Foto, Terkirim,SerialDevice,AppVersion);
//            listView.setAdapter(adapterAbsensi);
//        }
//    }
//}



