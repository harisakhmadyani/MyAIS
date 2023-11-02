package com.sambu.myais.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sambu.myais.R;

public class AdapterAnggota extends ArrayAdapter {
    String[] Jabatan;
    String[] NIK;
    String[] Nama;

    public AdapterAnggota(Context context, String[] Nama, String[] NIK, String[] Jabatan) {
        super(context, R.layout.listview_tenagakerja, R.id.Nama, Nama);
        this.Nama = Nama;
        this.NIK = NIK;
        this.Jabatan = Jabatan;
    }

    public View getView(int position, View converView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.listview_tenagakerja, parent, false);

        TextView nama = row.findViewById(R.id.Nama);
        TextView nik = row.findViewById(R.id.Nik);
        TextView jabatan = row.findViewById(R.id.Jabatan);

        nama.setText(Nama[position]);
        nik.setText(NIK[position]);
        jabatan.setText(Jabatan[position]);

        return row;
    }
}
