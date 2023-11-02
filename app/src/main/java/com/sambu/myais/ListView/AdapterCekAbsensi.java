package com.sambu.myais.ListView;

import static com.sambu.myais.R.id.KeteranganMasuk;
import static com.sambu.myais.R.id.scs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sambu.myais.R;

public class AdapterCekAbsensi extends ArrayAdapter {
//    String[] Jabatan;
    String[] Nik;
    String[] Nama;
    String[] KodeWilayah;
    String[] JamMasuk;
    String[] JamPulang;
    String[] KehadiranMasuk;
    String[] KehadiranPulang;
    String[] KodeSubPersilMasuk;
    String[] KodeSubPersilPulang;
    String[] KetMasuk;
    String[] KetPulang;

    public AdapterCekAbsensi(Context context, String[] Nama2, String[] Nik2,String[] KodeWilayah2,String[] JamMasuk2,String[] JamPulang2,String[] KehadiranMasuk2,String[] KehadiranPulang2, String[] KodeSubPersilMasuk2, String[] KodeSubPersilPulang2, String[] KetMasuk2, String[] KetPulang2) {
        super(context, R.layout.listview_cek_absensi, R.id.Nama, Nama2);
        this.Nama = Nama2;
        this.Nik = Nik2;
        this.KodeWilayah = KodeWilayah2;
        this.JamMasuk = JamMasuk2;
        this.JamPulang = JamPulang2;
        this.KehadiranMasuk = KehadiranMasuk2;
        this.KehadiranPulang = KehadiranPulang2;
        this.KodeSubPersilMasuk = KodeSubPersilMasuk2;
        this.KodeSubPersilPulang = KodeSubPersilPulang2;
        this.KetMasuk = KetMasuk2;
        this.KetPulang = KetPulang2;
//        this.Jabatan = Jabatan2;
//        this.Tanggal = Tanggal2;
//        this.KetAbsen = KetAbsen2;
    }

    public View getView(int position, View converView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.listview_cek_absensi, parent, false);

        TextView nama = row.findViewById(R.id.Nama);
        TextView nik = row.findViewById(R.id.Nik);
        TextView Wilayah = row.findViewById(R.id.Wilayah);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView jammasuk = row.findViewById(R.id.JamMasuk);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView jampulang = row.findViewById(R.id.JamPulang);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView kehadiranmasuk = row.findViewById(R.id.KehadiranMasuk);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView kehadiranpulang = row.findViewById(R.id.KehadiranPulang);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView subpersilmasuk = row.findViewById(R.id.SubPersilMasuk);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView subpersilpulang = row.findViewById(R.id.SubPersilPulang);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView ketmasuk = row.findViewById(R.id.KeteranganMasuk);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView ketpulang = row.findViewById(R.id.KeteranganPulang);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView scs = row.findViewById(R.id.scs);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView wrn = row.findViewById(R.id.wrn);

//        TextView kehadiran = row.findViewById(R.id.Kehadiran);

        nama.setText(Nama[position]);
        nik.setText(Nik[position]);
        Wilayah.setText(KodeWilayah[position]);
        jammasuk.setText(JamMasuk[position]);
        jampulang.setText(JamPulang[position]);
        kehadiranmasuk.setText(KehadiranMasuk[position]);
        kehadiranpulang.setText(KehadiranPulang[position]);
        subpersilmasuk.setText(KodeSubPersilMasuk[position]);
        subpersilpulang.setText(KodeSubPersilPulang[position]);
        ketmasuk.setText(KetMasuk[position]);
        ketpulang.setText(KetPulang[position]);

        if (!KehadiranMasuk[position].equals("-") && !KehadiranPulang[position].equals("-")) {
            scs.setVisibility(View.VISIBLE);
            wrn.setVisibility(View.GONE);
        }
        else {
            wrn.setVisibility(View.VISIBLE);
            scs.setVisibility(View.GONE);
        }

        return row;
    }
}
