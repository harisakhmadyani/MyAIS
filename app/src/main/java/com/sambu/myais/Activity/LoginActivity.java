package com.sambu.myais.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.R;
import com.sambu.myais.Retrofit.DeviceInfo;
import com.sambu.myais.Retrofit.JsonPlaceHolderApi;
import com.sambu.myais.SplashScreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button signin, restart;
    EditText userid, password;
    ImageButton lockpass;
    LinearLayout formisi, warning;
    ProgressBar loading;
    Spinner pancang, jenisjaringan;
    TextView appversion, serialapp;

    boolean locked = true;

    Double[] Latitude, Longitude;
    Integer[] Toleransi;
    String nomorSerial, APIName, APIUrl;
    String[] IDPancang, PancangName;
    String user, KodeWilayah, GroupAccess;

    JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin = findViewById(R.id.signin);
        restart = findViewById(R.id.restart);
        password = findViewById(R.id.password);
        lockpass = findViewById(R.id.lockpass);
        formisi = findViewById(R.id.formisi);
        warning = findViewById(R.id.warning);
        loading = findViewById(R.id.loading);
//        pancang = findViewById(R.id.pancang);
        userid  = findViewById(R.id.userid);
        jenisjaringan = findViewById(R.id.jenisjaringan);
        appversion = findViewById(R.id.appversion);
        serialapp = findViewById(R.id.serialapp);

        appversion.setText("App Version : " + getString(R.string.appversion));

        mintaIzin();
        getSerialTablet();
        getJenisJaringan();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Anda belum memasukkan password", Toast.LENGTH_SHORT).show();
                } else {
                    verifikasiTablet();
                }
            }
        });

        lockpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locked) {
                    lockpass.setImageResource(R.drawable.ic_baseline_key_off_24);
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    lockpass.setImageResource(R.drawable.ic_baseline_key_24);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                locked = !locked;
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
            }
        });
    }

    private void getJenisJaringan() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(this);
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Get("APIUrl");

        if (data.getCount() == 0) {
            Toast.makeText(this, "API Url tidak tersimpan", Toast.LENGTH_SHORT).show();
        } else {
//            String[] aNama = new String[data.getCount() + 1];
//            String[] aLinkAPI = new String[data.getCount() + 1];
            String[] aNama = new String[data.getCount()];
            String[] aLinkAPI = new String[data.getCount()];
//            aNama[0] = "- Pilih -";
//            aLinkAPI[0] = "-";
            int i = 0;
            while (data.moveToNext()) {
                aNama[i] = data.getString(1);
                aLinkAPI[i] = data.getString(2);
                i++;
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.custom_spinner_2, aNama);
            jenisjaringan.setAdapter(arrayAdapter);
            jenisjaringan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //if (position != 0) {
                        SharedPreferences sharedPreferences = getSharedPreferences("AbsensiPancang", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("pilihanjaringan", aNama[position]);
                        editor.putString("linkAPI", aLinkAPI[position]);
                        editor.apply();
                        APIUrl = aLinkAPI[position];
//                        getPancang();
                    //}
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void verifikasiTablet() {
        setInfo("loading");

        retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Map<String, String> fields = new HashMap<>();
        fields.put("SerialDevice", nomorSerial);
        fields.put("Device", Build.MANUFACTURER);
        fields.put("Model", Build.MODEL);
        fields.put("API", String.valueOf(Build.VERSION.SDK_INT));
        fields.put("AndroidVersion", String.valueOf(Build.VERSION.RELEASE));
        fields.put("APPVersion", getString(R.string.appversion));
//        fields.put("PancangID", String.valueOf(IDPancang[pancang.getSelectedItemPosition()]));
        fields.put("UserID", this.userid.getText().toString());
        fields.put("Password", this.password.getText().toString());

        Call<List<DeviceInfo>> call = jsonPlaceHolderApi.VerifikasiPerangkat(fields);

        String a = "";

        call.enqueue(new Callback<List<DeviceInfo>>() {
            @Override
            public void onResponse(Call<List<DeviceInfo>> call, Response<List<DeviceInfo>> response) {
                if (!response.isSuccessful()) {
                    setInfo("warning");
                    password.setText("");
                    Toast.makeText(LoginActivity.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                } else {
                    List<DeviceInfo> deviceInfos = response.body();
                    Toast.makeText(LoginActivity.this, deviceInfos.get(0).getStatus(), Toast.LENGTH_SHORT).show();
                    if (deviceInfos.get(0).getLanjut().equals("1")) {
//                        simpanDetailPancang();
                        KodeWilayah = deviceInfos.get(0).getKodeWilayah();
                        GroupAccess = deviceInfos.get(0).getGroupAccess();
                        simpanUser();
                    } else {
                        setInfo("finish");
                        password.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DeviceInfo>> call, Throwable t) {
                setInfo("warning");
                password.setText("");
                Toast.makeText(LoginActivity.this, "Koneksi bermasalah - gagal mengurai data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void simpanUser() {
        user = userid.getText().toString();

        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(LoginActivity.this.getApplicationContext());
        dataBaseAccess.open();

        Cursor data = dataBaseAccess.Get("User");
        if(data.getCount() == 0){
            if (dataBaseAccess.insertUser(user, KodeWilayah, GroupAccess)) {
                if(GroupAccess.equals("Mandor")){
                    startActivity(new Intent(this, MenuActivity.class));
                }else {
                    startActivity(new Intent(this, Menu2Activity.class));
                }
            } else {
                setInfo("finish");
                Toast.makeText(this, "Gagal Simpan Data", Toast.LENGTH_SHORT).show();
            }
        } else {
            if(GroupAccess.equals("Mandor")){
                startActivity(new Intent(this, MenuActivity.class));
            } else if (GroupAccess.equals("Adm-Dept")) {
                startActivity(new Intent(this, Menu2Activity.class));
            }
        }
    }

    private void setInfo(String info) {
        switch (info) {
            case "loading":
                loading.setVisibility(View.VISIBLE);
                formisi.setVisibility(View.GONE);
                warning.setVisibility(View.GONE);
                break;
            case "warning":
                loading.setVisibility(View.GONE);
                formisi.setVisibility(View.GONE);
                warning.setVisibility(View.VISIBLE);
                break;
            case "finish":
                loading.setVisibility(View.GONE);
                formisi.setVisibility(View.VISIBLE);
                warning.setVisibility(View.GONE);
                break;
        }
    }

    private void mintaIzin() {
        String[] Permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!hasPermissions(LoginActivity.this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, 1);
        }
    }

    private boolean hasPermissions(LoginActivity loginActivity, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && loginActivity != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(loginActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("HardwareIds")
    private void getSerialTablet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            nomorSerial = Build.getSerial();
            serialapp.setText("Serial Device : " + nomorSerial);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            nomorSerial = Build.SERIAL;
            serialapp.setText("Serial Device : " + nomorSerial);
        } else {
            nomorSerial = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            serialapp.setText("Serial Device : " + nomorSerial);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
    }
}