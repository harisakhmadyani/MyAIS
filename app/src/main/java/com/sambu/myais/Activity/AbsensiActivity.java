package com.sambu.myais.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.R;
import com.sambu.myais.Retrofit.JsonPlaceHolderApi;
import com.sambu.myais.Retrofit.ResponseStatus;
import com.sambu.myais.SplashScreen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.gms.location.FusedLocationProviderClient;

public class AbsensiActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    boolean kameraberjalan;
    boolean isButtonClicked;

    Integer Toleransi;
    Double SubPersilKelapaLat, SubPersilKelapaLong;

    Camera camera;

    ImageButton ambilgambar;
    RelativeLayout gambar;
    Spinner snama, skehadiran;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    TextView tanggal, pancang, latitude, longitude, akurasi, dilokasi, simcard, appserial, appversion, subpersilkelapa, kodeabsen;
    EditText keterangan;

    File filefoto;
    Bitmap fotokamera;
    LocationManager locationManager;

    byte[] bytefoto;
    Integer[] aIDKehadiran, aRegNo;
    String[] aIDPersonel, aKehadiran, aNama, aShortName;
    String nomorSerial, pilihanNamaTK, tanggalAPI, linkAPI, KodeSubPersil, KodeAbsen, UserID, KodeWilayah;
    Integer pilihanRegNoTK;

    //untuk dialog
    Button ya, tidak;
    ImageView hasilfoto;

    JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;

    Handler handler = new Handler();
    Runnable runnable;

    //map
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfAPI = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "SimpleDateFormat", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(3);

        ambilgambar = findViewById(R.id.ambilgambar);
        gambar = findViewById(R.id.gambar);
        snama = findViewById(R.id.snama);
        skehadiran = findViewById(R.id.skehadiran);
        tanggal = findViewById(R.id.tanggal);
        kodeabsen = findViewById(R.id.kodeabsen);
        subpersilkelapa = findViewById(R.id.subpersilkelapa);
        latitude = findViewById(R.id.latidude);
        longitude = findViewById(R.id.longitude);
        akurasi = findViewById(R.id.akurasi);
        dilokasi = findViewById(R.id.dilokasi);
        simcard = findViewById(R.id.simcard);
        appserial = findViewById(R.id.appserial);
        appversion = findViewById(R.id.appversion);
        keterangan = findViewById(R.id.keterangan);

        pilihanRegNoTK = getIntent().getExtras().getInt("RegNo");
        pilihanNamaTK = getIntent().getExtras().getString("Nama");
        KodeAbsen = getIntent().getExtras().getString("KodeAbsen");

        SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
        linkAPI = sharedPreferences.getString("linkAPI", "-");

        appversion.setText(getString(R.string.appversion));

        getSerialTablet();
        cekSimCard();
        getUser();

        //untuk mencatat jika ada error di aplikasi
        try {
            Process exec = Runtime.getRuntime().exec("logcat -d");
            Runtime.getRuntime().exec("logcat -f /storage/emulated/0/AbsensiPancang.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        surfaceView.setFocusableInTouchMode(true);
        surfaceView.setFocusable(true);
        surfaceView.requestFocus();

        getTenagaKerja();
        getKehadiran();

        if(KodeAbsen.equals("Absen Masuk")){
            getSubPersilKelapaMasuk();
        } else {
            getSubPersilKelapa();
        }

        getLokasi();
        getLokasiAPI();
//        getLinkAPI();

        ambilgambar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(AbsensiActivity.this);
                dataBaseAccess.open();


                isButtonClicked = false;
//                tanggalAPI

//                String query = "";
//                if(KodeAbsen.equals("Absen Istirahat")){
//                    query = "SELECT * FROM Absensi WHERE KodeAbsen = 'Absen Masuk' AND SUBSTR(Tanggal, 1, 10) = SUBSTR('" + tanggalAPI +"', 1, 10) AND RegNo ='" + pilihanRegNoTK + "'";
//                } else if(KodeAbsen.equals("Absen Masuk 2")){
//                    query = "SELECT * FROM Absensi WHERE KodeAbsen = 'Absen Istirahat' AND SUBSTR(Tanggal, 1, 10) = SUBSTR('" + tanggalAPI +"', 1, 10) AND RegNo ='" + pilihanRegNoTK + "'";
//                } else if(KodeAbsen.equals("Absen Pulang")){
//                    if(KodeWilayah.equals("KIN1") || KodeWilayah.equals("KIN2")){
//                        query = "SELECT * FROM Absensi WHERE KodeAbsen = 'Absen Masuk 2' AND SUBSTR(Tanggal, 1, 10) = SUBSTR('" + tanggalAPI +"', 1, 10) AND RegNo ='" + pilihanRegNoTK + "'";
//                    } else {
//                        query = "SELECT * FROM Absensi WHERE KodeAbsen = 'Absen Masuk' AND SUBSTR(Tanggal, 1, 10) = SUBSTR('" + tanggalAPI +"', 1, 10) AND RegNo ='" + pilihanRegNoTK + "'";
//                    }
//                }
//                Cursor cekdata = dataBaseAccess.GetCustom(query);

                if (snama.getSelectedItemPosition() == 0 || skehadiran.getSelectedItemPosition() == 0) {
                    Toast.makeText(AbsensiActivity.this, "Nama/kehadiran belum dipilih", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(akurasi.getText().toString()) > 50.0) {
                    Toast.makeText(AbsensiActivity.this, "Lokasi anda tidak terdeteksi akurat", Toast.LENGTH_SHORT).show();
                } else if (dilokasi.getText().toString().equals("Tidak")) {
                    Toast.makeText(AbsensiActivity.this, "Anda berada diliuar jangkauan lokasi", Toast.LENGTH_SHORT).show();
                } else if (Settings.Global.getInt(getContentResolver(), "auto_time", 0) == 0) {
                    Toast.makeText(AbsensiActivity.this, "Pastikan jam anda di set otomatis", Toast.LENGTH_SHORT).show();
                }
//                else if (KodeAbsen.equals("Absen Istirahat") && cekdata.getCount() == 0) {
//                    Toast.makeText(AbsensiActivity.this, "Anda belum melakukan absen masuk", Toast.LENGTH_SHORT).show();
//                } else if (KodeAbsen.equals("Absen Masuk 2") && cekdata.getCount() == 0) {
//                    Toast.makeText(AbsensiActivity.this, "Anda belum melakukan absen istirahat", Toast.LENGTH_SHORT).show();
//                } else if (KodeAbsen.equals("Absen Pulang") && cekdata.getCount() == 0 && (KodeWilayah.equals("KIN1") || KodeWilayah.equals("KIN2"))) {
//                    Toast.makeText(AbsensiActivity.this, "Anda belum melakukan absen masuk 2", Toast.LENGTH_SHORT).show();
//                } else if (KodeAbsen.equals("Absen Pulang") && cekdata.getCount() == 0) {
//                    Toast.makeText(AbsensiActivity.this, "Anda belum melakukan absen masuk", Toast.LENGTH_SHORT).show();
//                }
                else if (keterangan.getText().toString().equals("")){
                    Toast.makeText(AbsensiActivity.this, "Anda belum memasukkan keterangan", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AbsensiActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_alert_absensi, null);

                    ya = view.findViewById(R.id.ya);
                    tidak = view.findViewById(R.id.tidak);
                    hasilfoto = view.findViewById(R.id.hasilfoto);

                    buatGambar();

                    builder.setCancelable(true);
                    builder.setView(view);

                    AlertDialog alertDialog = builder.create();
//
                    tidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    ya.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isButtonClicked) {
                                isButtonClicked = true;
                                simpanData();
                            }
                        }
                    });

                    alertDialog.show();
                }
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
                KodeWilayah = data.getString(1);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void cekSimCard() {
        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int statuskartu = telMgr.getSimState(0);
        simcard.setText(statuskartu == 0 ? "Not Available" : "Available");
    }

    private void buatGambar() {
        this.fotokamera = Bitmap.createScaledBitmap(fotokamera, gambar.getHeight(), gambar.getWidth(), false);
        Matrix matrix = new Matrix();
        matrix.postRotate(90.0f);
        Bitmap bitmap = fotokamera;
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), fotokamera.getHeight(), matrix, true);
        gambar.draw(new Canvas(rotatedBitmap));
        createFoto(Bitmap.createScaledBitmap(rotatedBitmap, 800, 1200, false));
    }

    private void createFoto(Bitmap fotojadi) {
        hasilfoto.setImageBitmap(fotojadi);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        fotojadi.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        filefoto = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpeg");
        bytefoto = bos.toByteArray();
        try {
            FileOutputStream fos = new FileOutputStream(this.filefoto);
            fos.write(this.bytefoto);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void simpanData(){
        handler.removeCallbacks(runnable);
        Integer regno = aRegNo[snama.getSelectedItemPosition()];
        String kehadiran = aShortName[skehadiran.getSelectedItemPosition()];
        double lat = Double.parseDouble(latitude.getText().toString());
        double lng = Double.parseDouble(longitude.getText().toString());
        String ket = keterangan.getText().toString();

        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();

        boolean isInserted = dataBaseAccess.insertAbsensi(UserID, regno, kehadiran, KodeAbsen, tanggalAPI, KodeSubPersil, lat, lng, this.bytefoto,false, nomorSerial, getString(R.string.appversion), ket);
//        boolean isInserted = true;

        if (camera != null) {
            kameraberjalan = false;
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.lock();
            camera.release();
            camera = null;
        }

        if (isInserted) {
            ambilgambar.setVisibility(View.GONE);
//            kirimAbsensi();
        } else {
            ambilgambar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Absensi gagal disimpan", Toast.LENGTH_SHORT).show();
        }
//        startActivity(new Intent(this, AbsensiActivity.class));
        Intent intent = new Intent(this, ChooseAnggotaActivity.class);
        intent.putExtra("Kode", KodeAbsen);
        startActivity(intent);
//        Log.e("","datanya2 "+pilihanRegNoTK+" / "+pilihanNamaTK+" / "+KodeAbsen);
    }

    void kirimAbsensi(){
        retrofit = new Retrofit.Builder()
            .baseUrl(linkAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        HashMap<String, RequestBody> fields = new HashMap<>();

        fields.put("UserID", createPartFromString(UserID));
        fields.put("RegNo", createPartFromString(aRegNo[snama.getSelectedItemPosition()].toString()));
        fields.put("Kehadiran", createPartFromString(aShortName[skehadiran.getSelectedItemPosition()]));
        fields.put("KodeAbsen", createPartFromString(KodeAbsen));
        fields.put("Tanggal", createPartFromString(tanggalAPI));
        fields.put("KodeSubPersil", createPartFromString(KodeSubPersil));
        fields.put("Latitude", createPartFromString(latitude.getText().toString()));
        fields.put("Longitude", createPartFromString(longitude.getText().toString()));
        fields.put("SerialDevice", createPartFromString(nomorSerial));
        fields.put("AppVersion", createPartFromString(getString(R.string.appversion)));
        fields.put("Keterangan", createPartFromString(keterangan.getText().toString()));


        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), filefoto);
        MultipartBody.Part kirimfoto = MultipartBody.Part.createFormData("foto", filefoto.toString(), reqFile);

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<ResponseStatus>> call = jsonPlaceHolderApi.InsertAbsensi(fields, kirimfoto);

        call.enqueue(new Callback<List<ResponseStatus>>() {
            @Override
            public void onResponse(Call<List<ResponseStatus>> call, Response<List<ResponseStatus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AbsensiActivity.this, "Absensi gagal terkirim", Toast.LENGTH_SHORT).show();
                } else {
                    List<ResponseStatus> responseStatuses = response.body();
                    if(responseStatuses.get(0).getStatus().equals("logout")){
                        Toast.makeText(AbsensiActivity.this, "Perangkat telah diganti", Toast.LENGTH_SHORT).show();
                        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(AbsensiActivity.this);
                        dataBaseAccess.open();
                        dataBaseAccess.DeleteAll("PersonelTable").getCount();
                        dataBaseAccess.DeleteAll("KehadiranTable").getCount();
                        dataBaseAccess.DeleteAll("PancangTable").getCount();
                        startActivity(new Intent(AbsensiActivity.this, SplashScreen.class));
                    } else if(responseStatuses.get(0).getStatus().equals("renew")){
                        Toast.makeText(AbsensiActivity.this, "Absensi gagal terkirim, perbaharui aplikasi ketika melakukan absensi", Toast.LENGTH_SHORT).show();
                    } else {
                        updateLastAbsensi();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ResponseStatus>> call, Throwable t) {
                Toast.makeText(AbsensiActivity.this, "Absensi gagal terkirim", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLastAbsensi() {
        String ID = null;

        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(this);
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.GetLast("Absensi");

        if (data.getCount() == 0) {
            Toast.makeText(this, "Gagal mengambil absensi terakhir", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                ID = String.valueOf(data.getInt(0));
            }
            if (dataBaseAccess.updateAbsensi(ID)) {
                Toast.makeText(this, "Absensi berhasil dikirim", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Absensi gagal terkirim", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getTenagaKerja() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Where("TenagaKerja", "RegNo=" + pilihanRegNoTK);
        if (data.getCount() == 0) {
            Toast.makeText(this, "Data tenaga kerja tidak ditemukan", Toast.LENGTH_SHORT).show();
            aRegNo[0] = 0;
            aNama[0] = "Tidak Ada Data";
        } else {
            aRegNo = new Integer[(data.getCount() + 1)];
            aNama = new String[(data.getCount() + 1)];

            aRegNo[0] = 0;
            aNama[0] = "- Pilih -";

            int i = 1;
            while (data.moveToNext()) {
                aRegNo[i] = data.getInt(0);
                aNama[i] = data.getString(2);
                i++;
            }
        }
        snama.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, aNama));
        snama.setSelection(Arrays.asList(aRegNo).indexOf(pilihanRegNoTK));
    }

    private void getKehadiran() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor kehadiran = dataBaseAccess.Get("Kehadiran");
        if (kehadiran.getCount() == 0) {
            Toast.makeText(this, "Data kehadiran tidak ditemukan", Toast.LENGTH_SHORT).show();
            aIDKehadiran[0] = 0;
            aKehadiran[0] = "Tidak Ada Data";
            aShortName[0] = "-";
        } else {
            aIDKehadiran = new Integer[(kehadiran.getCount() + 1)];
            aKehadiran = new String[(kehadiran.getCount() + 1)];
            aShortName = new String[(kehadiran.getCount() + 1)];

            aIDKehadiran[0] = 0;
            aKehadiran[0] = "- Pilih -";
            aShortName[0] = "- None -";
            int i = 1;

            while (kehadiran.moveToNext()) {
                aIDKehadiran[i] = kehadiran.getInt(0);
                aKehadiran[i] = kehadiran.getString(1);
                aShortName[i] = kehadiran.getString(2);
                i++;
            }
        }
        skehadiran.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, aKehadiran));
    }

//    private void getIDPancang() {
//        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
//        dataBaseAccess.open();
//
//        Cursor data = dataBaseAccess.Get("PancangTable");
//
//        if (data.getCount() == 0) {
//            startActivity(new Intent(this, SplashScreen.class));
//        } else {
//            while (data.moveToNext()) {
//                KodeSubPersil = data.getString(0);
//                pancang.setText(data.getString(1));
//                PancangLat = data.getDouble(2);
//                PancangLong = data.getDouble(3);
//                Toleransi = data.getInt(4);
//            }
//        }
//    }

    private void getSubPersilKelapaMasuk() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Where("SubPersilKelapa", "LokasiAbsensiMasuk = 1 AND ActiveLokasiMasuk = 1");

        if (data.getCount() == 0) {
            GetAlertFailed("Persil belum di set");
            KodeSubPersil = "0";
            subpersilkelapa.setText("-");
            SubPersilKelapaLat = 0.00;
            SubPersilKelapaLong = 0.00;
            Toleransi = 0;
        } else {
            while (data.moveToNext()) {
                KodeSubPersil = data.getString(0);
                subpersilkelapa.setText(data.getString(0));
                SubPersilKelapaLat = data.getDouble(1);
                SubPersilKelapaLong = data.getDouble(2);
                Toleransi = data.getInt(3);
            }
        }
    }

    private void getSubPersilKelapa() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Where("SubPersilKelapa", "Active=1");

        if (data.getCount() == 0) {
            GetAlertFailed("Persil belum di set");
                KodeSubPersil = "0";
                subpersilkelapa.setText("-");
                SubPersilKelapaLat = 0.00;
                SubPersilKelapaLong = 0.00;
                Toleransi = 0;
        } else {
            while (data.moveToNext()) {
                KodeSubPersil = data.getString(0);
                subpersilkelapa.setText(data.getString(0));
                SubPersilKelapaLat = data.getDouble(1);
                SubPersilKelapaLong = data.getDouble(2);
                Toleransi = data.getInt(3);
            }
        }
    }

    private void getLokasiAPI() {
        if (ActivityCompat.checkSelfPermission(AbsensiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AbsensiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AbsensiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AbsensiActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return;
        }

        if(latitude.getText().toString().equals("0")) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AbsensiActivity.this);
                    if (ActivityCompat.checkSelfPermission(AbsensiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AbsensiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AbsensiActivity.this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                        return;
                    }
                    Task<Location> task = fusedLocationProviderClient.getLastLocation();
                    task.addOnSuccessListener(location -> {
                        if (location != null) {
                            currentLocation = location;

                            if (currentLocation.isFromMockProvider()) {
                                Toast.makeText(AbsensiActivity.this, "Terdeteksi applikasi tiru lokasi", Toast.LENGTH_SHORT).show();
                                handler.removeCallbacks(runnable);
                                finish();
                            }

                            if (Settings.Global.getInt(getContentResolver(), "auto_time", 0) == 0) {
                                Toast.makeText(AbsensiActivity.this, "Pastikan jam anda di set otomatis", Toast.LENGTH_SHORT).show();
                                handler.removeCallbacks(runnable);
                                startActivity(new Intent(AbsensiActivity.this, MenuActivity.class));
                            }

                            boolean airplane = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
                            if(airplane){
                                Toast.makeText(AbsensiActivity.this, "Mohon nonaktifkan mode pesawat anda", Toast.LENGTH_SHORT).show();
                                handler.removeCallbacks(runnable);
                                startActivity(new Intent(AbsensiActivity.this, MenuActivity.class));
                            }

                            latitude.setText(String.valueOf(currentLocation.getLatitude()));
                            longitude.setText(String.valueOf(currentLocation.getLongitude()));
                            tanggal.setText(sdf.format(new Date(currentLocation.getTime())));
                            kodeabsen.setText(KodeAbsen);
                            tanggalAPI = sdfAPI.format(new Date(currentLocation.getTime()));
                            akurasi.setText(String.valueOf(currentLocation.getAccuracy()));

                            Location loc1 = new Location("");
                            loc1.setLatitude(SubPersilKelapaLat);
                            loc1.setLongitude(SubPersilKelapaLong);

                            Location loc2 = new Location("");
                            loc2.setLatitude(currentLocation.getLatitude());
                            loc2.setLongitude(currentLocation.getLongitude());

                            if (loc1.distanceTo(loc2) < Toleransi) {
                                dilokasi.setText("Ya");
                            } else {
                                dilokasi.setText("Tidak");
                            }
                        }
                    });
                    handler.postDelayed(runnable, 2000);
                }
            };
            runnable.run();
        } else {
            handler.removeCallbacks(runnable);
        }
    }

    private void getLokasi() {
        locationManager = (LocationManager) Objects.requireNonNull(getSystemService(Context.LOCATION_SERVICE));

        LocationListener locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            public void onLocationChanged(Location location) {
//                if (IDPancang != 10 && IDPancang != 8) {
//                    if (location.isFromMockProvider()) {
//                        Toast.makeText(AbsensiActivity.this, "Terdeteksi Aplikasi Tiru Lokasi", Toast.LENGTH_SHORT).show();
//                        locationManager.removeUpdates(this);
//                        finish();
//                    }
//                }

                if (location.isFromMockProvider()) {
                    Toast.makeText(AbsensiActivity.this, "Terdeteksi applikasi tiru lokasi", Toast.LENGTH_SHORT).show();
                    locationManager.removeUpdates(this);
                    finish();
                }

                if (Settings.Global.getInt(getContentResolver(), "auto_time", 0) == 0) {
                    Toast.makeText(AbsensiActivity.this, "Pastikan jam anda di set otomatis", Toast.LENGTH_SHORT).show();
                    locationManager.removeUpdates(this);
                    startActivity(new Intent(AbsensiActivity.this, MenuActivity.class));
                }

                boolean airplane = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
                if(airplane){
                    Toast.makeText(AbsensiActivity.this, "Mohon nonaktifkan mode pesawat anda", Toast.LENGTH_SHORT).show();
                    locationManager.removeUpdates(this);
                    startActivity(new Intent(AbsensiActivity.this, MenuActivity.class));
                }

                tanggal.setText(sdf.format(new Date(location.getTime())));
                tanggalAPI = sdfAPI.format(new Date(location.getTime()));

                NumberFormat formatter = new DecimalFormat("#0.000000");

                latitude.setText(formatter.format(location.getLatitude()).replace(",", "."));
                longitude.setText(formatter.format(location.getLongitude()).replace(",", "."));
                akurasi.setText(String.valueOf(location.getAccuracy()));
                kodeabsen.setText(KodeAbsen);

                if(!latitude.getText().toString().equals("0")){
                    handler.removeCallbacks(runnable);
                }

                Location loc1 = new Location("");
                loc1.setLatitude(SubPersilKelapaLat);
                loc1.setLongitude(SubPersilKelapaLong);

                Location loc2 = new Location("");
                loc2.setLatitude(location.getLatitude());
                loc2.setLongitude(location.getLongitude());

                if (loc1.distanceTo(loc2) < Toleransi) {
                    dilokasi.setText("Ya");
                } else {
                    dilokasi.setText("Tidak");
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Toast.makeText(AbsensiActivity.this, "Harap hidupkan GPS anda", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };
        if (ActivityCompat.checkSelfPermission(AbsensiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AbsensiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AbsensiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AbsensiActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return;
        }
        locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        camera = Camera.open();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (kameraberjalan) {
            camera.stopPreview();
        } else {
            camera.getParameters().setPreviewSize(width, height);
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.setPreviewCallback(new Camera.PreviewCallback() {
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        Camera.Parameters parameters = camera.getParameters();
                        YuvImage image = new YuvImage(data, parameters.getPreviewFormat(), parameters.getPreviewSize().width, parameters.getPreviewSize().height, null);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        image.compressToJpeg(new Rect(0, 0, parameters.getPreviewSize().width, parameters.getPreviewSize().height), 50, outputStream);
                        fotokamera = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size());
                    }
                });
                camera.setDisplayOrientation(90);
                camera.startPreview();
                kameraberjalan = true;
            } catch (IOException e) {
                Toast.makeText(this, "Gagal membuka kamera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (camera != null) {
            kameraberjalan = false;
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.lock();
            camera.release();
            camera = null;
        }
    }

    @SuppressLint({"HardwareIds", "SetTextI18n"})
    private void getSerialTablet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            nomorSerial = Build.getSerial();
            appserial.setText(nomorSerial);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            nomorSerial = Build.SERIAL;
            appserial.setText(nomorSerial);
        } else {
            nomorSerial = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            appserial.setText(nomorSerial);
        }
    }

//    private void getLinkAPI() {
//        if (linklokal) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(getString(R.string.linklocal_backup))
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//        } else {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(getString(R.string.linkpublic_backup))
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//        }
//        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//    }


    private void GetAlertFailed(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(AbsensiActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.failed_dialog,null);
        Button btnOK = view.findViewById(R.id.btnOK);
        TextView textfailed = view.findViewById(R.id.textfailed);
        builder.setCancelable(true);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        textfailed.setText(msg);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        textfailed.setText(msg);
        alertDialog.show();
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (camera != null) {
            kameraberjalan = false;
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.lock();
            camera.release();
            camera = null;
        }
        startActivity(new Intent(AbsensiActivity.this, MenuActivity.class));
    }
}