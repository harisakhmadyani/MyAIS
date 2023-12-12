package com.sambu.myais.ListView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sambu.myais.Activity.KecelakaanKerjaActivity;
import com.sambu.myais.Activity.ListKecelakaanKerjaActivity;
import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.R;
import com.sambu.myais.Retrofit.JsonPlaceHolderApi;
import com.sambu.myais.Retrofit.ResponseStatus;
import com.sambu.myais.SplashScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterKecelakaanKerja extends ArrayAdapter {
    Boolean[] Terkirim;
    Integer[] ID, RegNo, KecelakaanID ;
    String[] Nama, Tanggal, KodeSubPersil, Kecelakaan, KronologiKecelakaan, TanggalInd;
    String UserID, GroupAccess = "", DataSource = "";

    JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;


    public AdapterKecelakaanKerja(Context context, Integer[] ID, Integer[] RegNo, String[] Nama, String[] Tanggal, String[] KodeSubPersil, Integer[] KecelakaanID, String[] Kecelakaan, String[] KronologiKecelakaan, Boolean[] Terkirim, String[] TanggalInd) {
        super(context, R.layout.listview_kecelakaan_kerja, R.id.Nama, Nama);
        this.ID = ID;
        this.RegNo = RegNo;
        this.Nama = Nama;
        this.Tanggal = Tanggal;
        this.KodeSubPersil = KodeSubPersil;
        this.KecelakaanID = KecelakaanID;
        this.Kecelakaan = Kecelakaan;
        this.KronologiKecelakaan = KronologiKecelakaan;
        this.Terkirim = Terkirim;
        this.TanggalInd = TanggalInd;
    }

    @SuppressLint({"SetTextI18n", "RestrictedApi", "MissingInflatedId"})
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.listview_kecelakaan_kerja, parent, false);

        TextView tvNama = row.findViewById(R.id.Nama);
        TextView tvWaktu = row.findViewById(R.id.wkt);
        TextView tvPersil = row.findViewById(R.id.subPrsl);
        TextView tvTipeKec = row.findViewById(R.id.tpKecel);
        TextView tvKronologi = row.findViewById(R.id.kron);
        ImageView imSuccess = row.findViewById(R.id.success);
        Button kirim = row.findViewById(R.id.kirim);
        ProgressBar loading = row.findViewById(R.id.loading);
        TableLayout tblDtKec = row.findViewById(R.id.tblDtKec);

        tvNama.setText(Nama[position]);
        tvWaktu.setText(TanggalInd[position]);
        tvPersil.setText(KodeSubPersil[position]);
        tvTipeKec.setText(Kecelakaan[position]);
        tvKronologi.setText(KronologiKecelakaan[position]);

        getUser();


        if (Terkirim[position]) {
            kirim.setVisibility(View.GONE);
            imSuccess.setVisibility(View.VISIBLE);
        } else {
            kirim.setVisibility(View.VISIBLE);
        }

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirim.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                kirimKecelakaan(position, kirim, loading, imSuccess);
            }
        });

        tblDtKec.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Membuat AlertDialog untuk menampilkan opsi
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Pilih Aksi");

                // Tambahkan pilihan aksi
                builder.setItems(new CharSequence[]{"Hapus data"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                hapusData(position);
                                break;
                        }
                    }
                });

                // Tampilkan AlertDialog
                builder.show();
                return true; // Mengembalikan true menandakan bahwa event long click telah dihandle
            }
        });

        return row;
    }


    private void getUser() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Get("User");

        if (data.getCount() == 0) {
            Toast.makeText(getContext(), "User tidak ditemukan", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                UserID = data.getString(0);
                GroupAccess = data.getString(2);
            }
        }
    }

    private void hapusData(int posisi){
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getContext());
        dataBaseAccess.open();
        dataBaseAccess.DeleteWhere("KecelakaanKerja", "ID = " + ID[posisi]).getCount();
        Toast.makeText(getContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
        getContext().startActivity(new Intent(getContext(), ListKecelakaanKerjaActivity.class));
    }

    private void kirimKecelakaan(int posisi, Button kirim, ProgressBar loading, ImageView imSuccess) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        String linkAPI = sharedPreferences.getString("linkAPI", "-");

        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        DataSource = GroupAccess.equals("Mandor") ? "MyPAD":"PKBPayroll";

        Map<String, String> fields = new HashMap<>();
        fields.put("Tanggal", Tanggal[posisi]);
        fields.put("RegNo", RegNo[posisi].toString());
        fields.put("KodeSubPersil", KodeSubPersil[posisi]);
        fields.put("KecelakaanID", KecelakaanID[posisi].toString());
        fields.put("KronologiKecelakaan", KronologiKecelakaan[posisi]);
        fields.put("UserID", UserID);
        fields.put("DataSource", DataSource);

        Call<List<ResponseStatus>> call = jsonPlaceHolderApi.InsertKecelakaanKerja(fields);

        call.enqueue(new Callback<List<ResponseStatus>>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<List<ResponseStatus>> call, Response<List<ResponseStatus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Data gagal terkirim", Toast.LENGTH_SHORT).show();
                    kirim.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                } else {
                    List<ResponseStatus> responseStatuses = response.body();
                    if(responseStatuses.get(0).getStatus().equals("success")) {
                        updateData(ID[posisi], kirim, loading, imSuccess);
                    } else {
                        Toast.makeText(getContext(), "Data gagal terkirim", Toast.LENGTH_SHORT).show();
                        kirim.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onFailure(Call<List<ResponseStatus>> call, Throwable t) {
                Toast.makeText(getContext(), "Data gagal terkirim", Toast.LENGTH_SHORT).show();
                kirim.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    private void updateData(Integer id, Button kirim, ProgressBar loading, ImageView imSuccess) {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getContext());
        dataBaseAccess.open();

        if (dataBaseAccess.updateKecelakaan(id.toString())) {
            Toast.makeText(getContext(), "Data berhasil dikirim", Toast.LENGTH_SHORT).show();
            kirim.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            imSuccess.setVisibility(View.VISIBLE);
            return;
        }
        Toast.makeText(getContext(), "Data gagal terkirim", Toast.LENGTH_SHORT).show();
        kirim.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }
}
