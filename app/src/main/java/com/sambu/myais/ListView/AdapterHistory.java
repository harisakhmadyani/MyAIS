package com.sambu.myais.ListView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterHistory extends ArrayAdapter {
    Boolean[] Terkirim;
    byte[][] Foto;
    Double[] Latitude, Longitude;
    Integer[] ID, RegNo ;
    String[] NIK, Nama, KodeWilayah, Kehadiran, KodeAbsen, Tanggal, KodeSubPersil,SerialDevice, AppVersion, TanggalInd, Keterangan ;
    String UserID;

    String nomorSerial = "";
    boolean linklokal = false;

    JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;

    public AdapterHistory(Context context, Integer[] ID, Integer[] RegNo,String[] NIK,String[] Nama,String[] KodeWilayah , String[] Kehadiran , String [] KodeAbsen , String[] Tanggal,String[] KodeSubPersil , Double[] Latitude,Double[] Longitude , byte[][] Foto, Boolean[] Terkirim, String[] SerialDevice ,String[] AppVersion, String[] TanggalInd, String[] Keterangan) {
        super(context, R.layout.listview_history, R.id.Nama, Nama);
        this.ID = ID;
        this.Nama = Nama;
        this.NIK = NIK ;
        this.KodeAbsen = KodeAbsen;
        this.SerialDevice = SerialDevice;
        this.KodeWilayah = KodeWilayah;
        this.KodeSubPersil = KodeSubPersil;
        this.Foto = Foto;
        this.RegNo = RegNo;
        this.Kehadiran = Kehadiran;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Tanggal = Tanggal;
        this.Terkirim = Terkirim;
        this.AppVersion = AppVersion;
        this.TanggalInd = TanggalInd;
        this.Keterangan = Keterangan;
    }

    @SuppressLint({"SetTextI18n", "RestrictedApi", "MissingInflatedId"})
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.listview_history, parent, false);

        ImageView ivFoto = row.findViewById(R.id.Foto);
        TextView tvTerkirim = row.findViewById(R.id.Terkirim);
        TextView tvNama = row.findViewById(R.id.Nama);
        TextView tvTanggal = row.findViewById(R.id.Tanggal);
        TextView tvKehadiran = row.findViewById(R.id.Kehadiran);
        TextView tvLokasi = row.findViewById(R.id.Lokasi);
        ImageView imSuccess = row.findViewById(R.id.success);
        Button kirim = row.findViewById(R.id.kirim);
        ProgressBar loading = row.findViewById(R.id.loading);
        TextView tvKeterangan = row.findViewById(R.id.keterangan);

        tvNama.setText(Nama[position]);
        tvTanggal.setText(TanggalInd[position]);
        tvKehadiran.setText(Kehadiran[position]);
        tvLokasi.setText(KodeSubPersil[position]);
        tvKeterangan.setText(Keterangan[position]);

        DecimalFormat decimalFormat = new DecimalFormat("#0.000000");

//        tvLokasi.setText(decimalFormat.format(this.Latitude[position]) + ", " + decimalFormat.format(this.Longitude[position]));

        getUser();

        ivFoto.setImageBitmap(BitmapFactory.decodeByteArray(Foto[position], 0, Foto[position].length));

        if (Terkirim[position]) {
            tvTerkirim.setText("Absensi Terkirim");
            kirim.setVisibility(View.GONE);
            imSuccess.setVisibility(View.VISIBLE);
        } else {
            tvTerkirim.setText("Absensi Tidak Terkirim");
//            tvTerkirim.setTextColor(SupportMenu.CATEGORY_MASK);
            tvTerkirim.setTypeface(tvTerkirim.getTypeface(), Typeface.BOLD);
            kirim.setVisibility(View.VISIBLE);
        }

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirim.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                kirimAbsensi(position, tvTerkirim, kirim, loading, imSuccess);
            }
        });

//        ivFoto.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(AdapterHistory.this.getContext());
//                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.dialog_image, null);
//
//                builder.setCancelable(true);
//
//                Button tutup = view.findViewById(R.id.tutup);
//                ImageView gambarabsensi = view.findViewById(R.id.gambarabsensi);
//
//                builder.setView(view);
//                AlertDialog alertDialog = builder.create();
//
//                //gambarabsensi.setImageBitmap(BitmapFactory.decodeByteArray(AdapterHistory.this.Foto[position], 0, AdapterHistory.this.Foto[position].length));
//
//                tutup.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                alertDialog.show();
//            }
//        });

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
            }
        }
    }

    private void kirimAbsensi(int posisi, TextView tvTerkirim, Button kirim, ProgressBar loading, ImageView imSuccess) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        String linkAPI = sharedPreferences.getString("linkAPI", "-");

        retrofit = new Retrofit.Builder()
                .baseUrl(linkAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getSerialTablet();

        HashMap<String, RequestBody> fields = new HashMap<>();
        fields.put("UserID", createPartFromString(UserID));
        fields.put("RegNo", createPartFromString(RegNo[posisi].toString()));
        fields.put("Kehadiran", createPartFromString(Kehadiran[posisi]));
        fields.put("KodeAbsen", createPartFromString(KodeAbsen[posisi]));
        fields.put("Tanggal", createPartFromString(Tanggal[posisi]));;
        fields.put("KodeSubPersil", createPartFromString(KodeSubPersil[posisi]));
        fields.put("Latitude", createPartFromString(String.valueOf(Latitude[posisi])));
        fields.put("Longitude", createPartFromString(String.valueOf(Longitude[posisi])));
        fields.put("SerialDevice", createPartFromString(nomorSerial));
        fields.put("AppVersion", createPartFromString(AppVersion[posisi]));
        fields.put("Keterangan", createPartFromString(Keterangan[posisi]));

        File foto = createTempFile(this.Foto[posisi]);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), foto);
        MultipartBody.Part kirimfoto = MultipartBody.Part.createFormData("foto", foto.toString(), reqFile);

        Call<List<ResponseStatus>> call = jsonPlaceHolderApi.InsertAbsensi(fields, kirimfoto);

        call.enqueue(new Callback<List<ResponseStatus>>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<List<ResponseStatus>> call, Response<List<ResponseStatus>> response) {
                if (!response.isSuccessful()) {
                    List<ResponseStatus> responseStatuses = response.body();
                    Toast.makeText(getContext(), "Absensi gagal terkirim", Toast.LENGTH_SHORT).show();
//                    tvTerkirim.setTextColor(SupportMenu.CATEGORY_MASK);
                    tvTerkirim.setTypeface(tvTerkirim.getTypeface(), Typeface.BOLD);
                    kirim.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                } else {
                    List<ResponseStatus> responseStatuses = response.body();
                    if(responseStatuses.get(0).getStatus().equals("logout")){
                        Toast.makeText(getContext(), "Perangkat telah diganti", Toast.LENGTH_SHORT).show();
                        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getContext());
                        dataBaseAccess.open();

                        dataBaseAccess.DeleteAll("PersonelTable").getCount();
                        dataBaseAccess.DeleteAll("KehadiranTable").getCount();
                        dataBaseAccess.DeleteAll("PancangTable").getCount();
                        getContext().startActivity(new Intent(getContext(), SplashScreen.class));
                    } else if(responseStatuses.get(0).getStatus().equals("renew")){
                        Toast.makeText(getContext(), "Absensi gagal terkirim, perbaharui aplikasi ketika melakukan absensi", Toast.LENGTH_SHORT).show();
                    } else {
                        updateData(ID[posisi], tvTerkirim, kirim, loading, imSuccess);
                    }
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onFailure(Call<List<ResponseStatus>> call, Throwable t) {
                Toast.makeText(getContext(), "Absensi gagal terkirim", Toast.LENGTH_SHORT).show();
//                tvTerkirim.setTextColor(SupportMenu.CATEGORY_MASK);
                tvTerkirim.setTypeface(tvTerkirim.getTypeface(), Typeface.BOLD);
                kirim.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        });
    }




    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    private void updateData(Integer id, TextView tvTerkirim, Button kirim, ProgressBar loading, ImageView imSuccess) {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getContext());
        dataBaseAccess.open();

        if (dataBaseAccess.updateAbsensi(id.toString())) {
            Toast.makeText(getContext(), "Absensi berhasil dikirim", Toast.LENGTH_SHORT).show();
            tvTerkirim.setText("Absensi Terkirim");
//            tvTerkirim.setTextColor(-1);
            kirim.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            imSuccess.setVisibility(View.VISIBLE);
            return;
        }

        Toast.makeText(getContext(), "Absensi gagal terkirim", Toast.LENGTH_SHORT).show();
        tvTerkirim.setText("Absensi Tidak Terkirim");
//        tvTerkirim.setTextColor(SupportMenu.CATEGORY_MASK);
        tvTerkirim.setTypeface(tvTerkirim.getTypeface(), Typeface.BOLD);
        kirim.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    private File createTempFile(byte[] bytefoto) {
        File foto = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpeg");
        try {
            FileOutputStream fos = new FileOutputStream(foto);
            fos.write(bytefoto);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foto;
    }

    private void getSerialTablet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            nomorSerial = Build.getSerial();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            nomorSerial = Build.SERIAL;
        } else {
            nomorSerial = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

//    private void getLinkAPI() {
//        if (linklokal) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(getContext().getString(R.string.linklocal_backup))
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//        } else {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(getContext().getString(R.string.linkpublic_backup))
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//        }
//        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//    }
}
