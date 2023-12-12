package com.sambu.myais.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.sambu.myais.R;

public class AdapterMonKecelakaan extends ArrayAdapter {
    String[] Nama;
    String[] NIK;
    String[] KodeWilayah;
    String[] Waktu;
    String[] KodeSubPersil;
    String[] TipeKecelakaan;
    String[] Kronologi;
    String[] TanggalInd;
    Integer[] StatusVerification;

    public AdapterMonKecelakaan(Context context, String[] nama, String[] nik, String[] kodewilayah, String[] waktu, String[] kodesubpersil, String[] tipekecelakaan, String[] kronologi, String[] tanggalind, Integer[] statusverification) {
        super(context, R.layout.listview_mon_kecelakaan_kerja, R.id.Nama, nama);
        this.NIK = nik;
        this.Nama = nama;
        this.KodeWilayah = kodewilayah;
        this.Waktu = waktu;
        this.KodeSubPersil = kodesubpersil;
        this.TipeKecelakaan = tipekecelakaan;
        this.Kronologi = kronologi;
        this.TanggalInd = tanggalind;
        this.StatusVerification = statusverification;
    }

    public View getView(int position, View converView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.listview_mon_kecelakaan_kerja, parent, false);

        TextView nama = row.findViewById(R.id.Nama);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView nik = row.findViewById(R.id.NIK);
        TextView wilayah = row.findViewById(R.id.Wilayah);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView wkt = row.findViewById(R.id.wkt);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView lks = row.findViewById(R.id.lks);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tpKec = row.findViewById(R.id.tpKec);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView kron = row.findViewById(R.id.kron);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView sts = row.findViewById(R.id.sts);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView crvw = row.findViewById(R.id.cardView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View vw = row.findViewById(R.id.view);

        nama.setText(Nama[position]);
        nik.setText(NIK[position]);
        wilayah.setText(KodeWilayah[position]);
        wkt.setText(TanggalInd[position]);
        lks.setText(KodeSubPersil[position]);
        tpKec.setText(TipeKecelakaan[position]);
        kron.setText(Kronologi[position]);

        if(StatusVerification[position] == 0){
            sts.setText("Proses verifikasi");
            sts.setTextColor(Color.parseColor("#FFBE52"));
            crvw.setCardBackgroundColor(Color.parseColor("#FFBE52"));
            vw.setBackgroundColor(Color.parseColor("#FFBE52"));
        } else if (StatusVerification[position] == 1){
            sts.setText("Terverifikasi");
            sts.setTextColor(Color.parseColor("#4CAF50"));
            crvw.setCardBackgroundColor(Color.parseColor("#4CAF50"));
            vw.setBackgroundColor(Color.parseColor("#4CAF50"));
        } else {
            sts.setText("Ditolak");
            sts.setTextColor(Color.parseColor("#F44336"));
            crvw.setCardBackgroundColor(Color.parseColor("#F44336"));
            vw.setBackgroundColor(Color.parseColor("#F44336"));
        }
        return row;
    }
}
