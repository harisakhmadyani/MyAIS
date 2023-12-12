package com.sambu.myais.Retrofit;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    @GET("Pancang/GetPancangByID/{id}")
    Call<List<Pancang>> GetPancangByID(@Path("id") int i);

    @GET("Pancang/GetPersonel/{id}")
    Call<List<Personel>> GetPersonel(@Path("id") int i);

    @GET("Absensi/getAndroidHistory/{id}/{tanggal}")
    Call<List<HistoryCekAbsensi>> cekAbsensi(@Path("id") String id, @Path("tanggal") String str);

    @GET("Absensi/GetMonKecelakaanKerja/{tanggal1}/{tanggal2}")
    Call<List<Kecelakaan>> GetMonKecelakaanKerja(@Path("tanggal1") String tgl1, @Path("tanggal2") String tgl2);

    @GET("Pancang/Get")
    Call<List<Pancang>> getPancang();



    @GET("TenagaKerja/GetByUser/{id}")
    Call<List<TenagaKerja>> GetTKByUser(@Path("id") String i);

    @GET("TenagaKerja/GetByDept/{id}")
    Call<List<TenagaKerja>> GetTKByDept(@Path("id") String i);

    @GET("SubPersilKelapa/GetByUser/{id}")
    Call<List<SubPersilKelapa>> GetSubPersilByUser(@Path("id") String i);

    @FormUrlEncoded
    @POST("UserDevice/VerifikasiPerangkat")
    Call<List<DeviceInfo>> VerifikasiPerangkat(@FieldMap Map<String, String> map);

    @GET("UserDevice/Logout/{id}")
    Call<List<DeviceInfo>> Logout(@Path("id") String i);

    @GET("Absensi/GetKehadiran")
    Call<List<Kehadiran>> GetKehadiran();

    @GET("Absensi/GetTipeKecelakaan")
    Call<List<TipeKecelakaan>> GetTipeKecelakaan();

    @POST("Absensi/InsertAbsensi")
    @Multipart
    Call<List<ResponseStatus>> InsertAbsensi(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part part);

    @FormUrlEncoded
    @POST("Absensi/InsertKecelakaanKerja")
    Call<List<ResponseStatus>> InsertKecelakaanKerja(@FieldMap Map<String, String> map);
}
