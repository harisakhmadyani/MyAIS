package com.sambu.myais.Activity.ui.masuk2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.ListView.AdapterHistory;
import com.sambu.myais.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Masuk2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Masuk2Fragment extends Fragment {
    String TanggalAPI;
    RadioButton today, notsend;
    SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfApiFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    ListView listViewMasuk2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Masuk2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Masuk2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Masuk2Fragment newInstance(String param1, String param2) {
        Masuk2Fragment fragment = new Masuk2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TanggalAPI = sdfApi.format(new Date());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_masuk2, container, false);
        listViewMasuk2 = view.findViewById(R.id.listViewMasuk2);

        today = view.findViewById(R.id.today);
        notsend = view.findViewById(R.id.notsend);

        today.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(today.isChecked()){
                    getData(TanggalAPI);
                }
            }
        });
        today.setChecked(true);

        notsend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(notsend.isChecked()){
                    getData("Not Send");
                }
            }
        });
        return view;
    }

    private void getData(String tanggalAPI) {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getActivity().getApplicationContext());
        dataBaseAccess.open();

        Cursor data;

        if (tanggalAPI.equals("Not Send")) {
            data = dataBaseAccess.Where("vwAbsensiNew", "Terkirim = 0 AND KodeAbsen = 'Absen Masuk 2' ORDER BY Nama");
        } else {
            data = dataBaseAccess.Where("vwAbsensiNew", "date(Tanggal) = date('" + tanggalAPI + "') AND KodeAbsen = 'Absen Masuk 2' ORDER BY Nama");
        }

        if (data.getCount() == 0) {
            listViewMasuk2.setAdapter(null);
        } else {
            Integer[] ID = new Integer[data.getCount()];
            Integer[] RegNo = new Integer[data.getCount()];
            String[] NIK = new  String[data.getCount()];
            String[] Nama = new String[data.getCount()];
            String[] KodeWilayah = new  String[data.getCount()];
            String[] Kehadiran = new String[data.getCount()];
            String[] KodeAbsen = new String[data.getCount()];
            String[] Tanggal = new String[data.getCount()];
            String[] KodeSubPersil = new String[data.getCount()];
            Double[] Latitude = new Double[data.getCount()];
            Double[] Longitude = new Double[data.getCount()];
            byte[][] Foto = new byte[data.getCount()][];
            Boolean[] Terkirim = new Boolean[data.getCount()];
            String[] SerialDevice = new String[data.getCount()];
            String[] AppVersion = new String[data.getCount()];
            String[] TanggalInd = new String[data.getCount()];
            String[] Keterangan = new String[data.getCount()];
            int i = 0;
            while (data.moveToNext()) {
                ID[i] = data.getInt(0);
                RegNo[i] = data.getInt(1);
                NIK[i] = data.getString(2);
                Nama[i] = data.getString(3);
                KodeWilayah[i] = data.getString(4);
                Kehadiran[i] = data.getString(5);
                KodeAbsen[i] = data.getString(7);
                Tanggal[i] = data.getString(8);
                KodeSubPersil[i] = data.getString(9);
                Latitude[i] = data.getDouble(10);
                Longitude[i] = data.getDouble(11);
                Foto[i] = data.getBlob(12);
                Terkirim[i] = data.getInt(13) > 0;
                SerialDevice[i] = data.getString(14);
                AppVersion[i] = data.getString(15);

                try {
                    TanggalInd[i] = sdf.format(Objects.requireNonNull(sdfApiFull.parse(data.getString(8))));
                } catch (ParseException e) {
                    TanggalInd[i] = data.getString(8);
                }

                Keterangan[i] = data.getString(16);

                i++;
            }
            listViewMasuk2.setAdapter(new AdapterHistory(getActivity().getApplicationContext(),ID, RegNo, NIK, Nama, KodeWilayah, Kehadiran, KodeAbsen, Tanggal, KodeSubPersil, Latitude, Longitude, Foto, Terkirim,SerialDevice,AppVersion, TanggalInd, Keterangan));
            listViewMasuk2.setVisibility(View.VISIBLE);
        }
    }
}