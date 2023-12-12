package com.sambu.myais.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sambu.myais.DataBase.DataBaseAccess;
import com.sambu.myais.R;
import com.sambu.myais.SplashScreen;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KecelakaanKerjaActivity extends AppCompatActivity {

    Integer[] aRegNo;
    String[] aNama, aSubPersil;

    Integer pilihanRegNoTK = 0, pilihanTipeKecelakaan = 0;

    String pilihanSubPersil = "- Pilih -", wkt = "", krono = "", UserID = "", GroupAccess = "", lksiDept ="";

    Spinner regNo, subPersil;
//    LinearLayout checkboxContainer;

    EditText waktu, kronologi, lokasiDept;

    TextView lblSubPersil, lblLokasiDept;

    RadioGroup radioGroup;

    Button btnSimpan;

    HashMap<CheckBox, Integer> checkBoxDataMap = new HashMap<>();
    HashMap<RadioButton, Integer> radioButtonDataMap = new HashMap<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kecelakaan_kerja);

        regNo = findViewById(R.id.regNo);
        lblSubPersil = findViewById(R.id.lblSubPersil);
        subPersil = findViewById(R.id.subPersil);
        waktu = findViewById(R.id.waktu);
        radioGroup = findViewById(R.id.radioGroup);
        btnSimpan = findViewById(R.id.btnSimpan);
        kronologi = findViewById(R.id.kronologi);
        lblLokasiDept = findViewById(R.id.lblLksDept);
        lokasiDept = findViewById(R.id.lksDept);
//        checkboxContainer = findViewById(R.id.checkbox_container);

        getUser();
        setHideForm();
        getTenagaKerja();
        getSubPersil();
        getTipeKecelakaan();

//        for (Map.Entry<CheckBox, Integer> entry : checkBoxDataMap.entrySet()) {
//            if (entry.getKey().isChecked()) {
//                pilihanTipeKecelakaan = entry.getValue();
//                // Lakukan sesuatu dengan data ini
//            }
//        }

//        btnSimpan.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // Membuat AlertDialog untuk menampilkan opsi
//                AlertDialog.Builder builder = new AlertDialog.Builder(KecelakaanKerjaActivity.this);
//                builder.setTitle("Pilih Aksi");
//
//                // Tambahkan pilihan aksi
//                builder.setItems(new CharSequence[]{"Aksi 1", "Aksi 2", "Aksi 3"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                // Aksi untuk pilihan pertama
//                                break;
//                            case 1:
//                                // Aksi untuk pilihan kedua
//                                break;
//                            case 2:
//                                // Aksi untuk pilihan ketiga
//                                break;
//                        }
//                    }
//                });
//
//                // Tampilkan AlertDialog
//                builder.show();
//                return true; // Mengembalikan true menandakan bahwa event long click telah dihandle
//            }
//        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wkt = waktu.getText().toString();

                for (Map.Entry<RadioButton, Integer> entry : radioButtonDataMap.entrySet()) {
                    if (entry.getKey().isChecked()) {
                        pilihanTipeKecelakaan = entry.getValue();
                        // Lakukan sesuatu dengan data ini
                    }
                }

                lksiDept = lokasiDept.getText().toString();
                krono = kronologi.getText().toString();

                String vld = validasiForm(pilihanRegNoTK, wkt, pilihanSubPersil, pilihanTipeKecelakaan, lksiDept, krono);

                if(vld.equals("valid")){
                    simpanData();
                } else {
                    Toast.makeText(KecelakaanKerjaActivity.this, vld, Toast.LENGTH_SHORT).show();
                }
            }
        });

        waktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(KecelakaanKerjaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(KecelakaanKerjaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int dayOfMonth, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String selectedDateTime = dateTimeFormat.format(calendar.getTime());
                                waktu.setText(selectedDateTime);
                            }
                        }, hourOfDay, minute, true);
                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
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
                GroupAccess = data.getString(2);
            }
        }
    }

    private void getTenagaKerja() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Get("TenagaKerja");
        if (data.getCount() == 0) {
//            Toast.makeText(this, "Data tenaga kerja tidak ditemukan", Toast.LENGTH_SHORT).show();
//            aRegNo[0] = 0;
//            aNama[0] = "Tidak Ada Data";
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

            regNo.setAdapter(new ArrayAdapter(this, R.layout.custom_spinner_4, aNama));
            regNo.setSelection(Arrays.asList(aRegNo).indexOf(pilihanRegNoTK));

            regNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pilihanRegNoTK = aRegNo[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void getSubPersil() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Order("SubPersilKelapa", "KodeSubPersil ASC");

        if (data.getCount() == 0) {
//            Toast.makeText(this, "Data persil tidak ditemukan", Toast.LENGTH_SHORT).show();
//            aSubPersil[0] = "Tidak Ada Data";
        } else {
            aSubPersil = new String[(data.getCount() + 1)];

            aSubPersil[0] = "- Pilih -";

            int i = 1;
            while (data.moveToNext()) {
                aSubPersil[i] = data.getString(0);
                i++;
            }

            subPersil.setAdapter(new ArrayAdapter(this, R.layout.custom_spinner_4, aSubPersil));
            subPersil.setSelection(Arrays.asList(aSubPersil).indexOf(pilihanSubPersil));

            subPersil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pilihanSubPersil = aSubPersil[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void getTipeKecelakaan() {
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
        dataBaseAccess.open();
        Cursor data = dataBaseAccess.Order("TipeKecelakaan", "Sort ASC");

        if (data.getCount() == 0) {
//            Toast.makeText(this, "Data tipe kecelakaan tidak ditemukan", Toast.LENGTH_SHORT).show();
        } else {
            int i = 0;
            while (data.moveToNext()) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setId(View.generateViewId());
                radioButton.setText(data.getString(1)); // Tampilan

                Integer dataToSave = data.getInt(0); // Data yang ingin disimpan
                radioButtonDataMap.put(radioButton, dataToSave);

                radioGroup.addView(radioButton);

                i++;
            }
        }
    }

    private String validasiForm(Integer rgNo, String wktu, String sbPersil, Integer tpKec, String lksDept, String krno) {
        if(rgNo == 0 ){
            return "Data korban belum dipilih";
        }

        if(wktu.equals("")){
            return "Data waktu kejadian belum dipilih";
        }

        if(GroupAccess.equals("Mandor")){
            if(sbPersil.equals("- Pilih -") ){
                return "Data persil kejadian belum dipilih";
            }
        } else {
            if(lksDept.equals("") ){
                return "Data lokasi kejadian belum diisi";
            }
        }

        if(tpKec == 0 ){
            return "Data tipe kecelakaan belum dipilih";
        }

        if(krno.equals("") ){
            return "Data kronologi kecelakaan belum diisi";
        }

        return "valid";
    }

    private void setHideForm(){
        if(GroupAccess.equals("Mandor")){
            lblLokasiDept.setVisibility(View.GONE);
            lokasiDept.setVisibility(View.GONE);
        } else {
            lblSubPersil.setVisibility(View.GONE);
            subPersil.setVisibility(View.GONE);
        }
    }

    private void simpanData(){
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(KecelakaanKerjaActivity.this);
        dataBaseAccess.open();

        pilihanSubPersil = GroupAccess.equals("Mandor") ? pilihanSubPersil : lksiDept;

        boolean isInserted = dataBaseAccess.insertKecelakaanKerja(wkt, pilihanRegNoTK, pilihanSubPersil, pilihanTipeKecelakaan, krono, false);

        if (isInserted) {
            GetAlertSuccess("Data berhasil disimpan");
        } else {
            GetAlertFailed("Data gagal disimpan");
        }
    }

    //    alert dialog success
    private void GetAlertSuccess(String msg){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(KecelakaanKerjaActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.succes_dialog,null);
        Button btnSuccess = view.findViewById(R.id.btnSuccess);
        TextView Txt = view.findViewById(R.id.TxtSuccess);
        builder.setCancelable(true);
        builder.setView(view);
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(KecelakaanKerjaActivity.this, ListKecelakaanKerjaActivity.class);
                startActivity(intent);
            }
        });
        Txt.setText(msg);
        alertDialog.show();
    }


    //    alert dialog failed
    private void GetAlertFailed(String msg){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(KecelakaanKerjaActivity.this);
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

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(KecelakaanKerjaActivity.this, ListKecelakaanKerjaActivity.class));
    }

//    private void getTipeKecelakaan() {
//        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getApplicationContext());
//        dataBaseAccess.open();
//        Cursor data = dataBaseAccess.Order("TipeKecelakaan", "Kecelakaan ASC");
//
//        if (data.getCount() == 0) {
//            Toast.makeText(this, "Data tipe kecelakaan tidak ditemukan", Toast.LENGTH_SHORT).show();
//        } else {
//            int i = 0;
//            while (data.moveToNext()) {
//                CheckBox checkBox = new CheckBox(this);
//                checkBox.setId(View.generateViewId());
//                checkBox.setText(data.getString(1)); // Tampilan
//
//                Integer dataToSave = data.getInt(0); // Data yang ingin disimpan
//                checkBoxDataMap.put(checkBox, dataToSave);
//
//                checkboxContainer.addView(checkBox);
//
//                i++;
//            }
//        }
//    }
}