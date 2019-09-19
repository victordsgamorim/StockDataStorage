package com.victor.stockpesistentdata.retrofit.service;

import com.victor.stockpesistentdata.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ItemService {

    @POST("stock")
    Call<Item> insert(@Body Item item);

    @PUT("stock/diff")
    Call<Item> update(@Header("item") Item item);

    @GET("stock")
    Call<List<Item>> list();

    @DELETE("stock/{id}")
    Call<Void> delete(@Path("id") int id);
}
