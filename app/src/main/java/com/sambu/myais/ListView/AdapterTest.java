package com.sambu.myais.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sambu.myais.R;

public class AdapterTest extends ArrayAdapter {
    String[] NIK;
    String[] Nama;

    public AdapterTest(Context context, String[] Nama, String[] NIK) {
        super(context, R.layout.custom_spinner_3, R.id.nik, NIK);
        this.Nama = Nama;
        this.NIK = NIK;
    }

    public View getView(int position, View converView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.custom_spinner_3, parent, false);

        TextView nik = row.findViewById(R.id.nik);

        nik.setText(NIK[position]);

        return row;
    }
}