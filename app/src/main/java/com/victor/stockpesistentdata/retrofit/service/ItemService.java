package com.victor.stockpesistentdata.retrofit.service;

import com.victor.stockpesistentdata.model.Item;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ItemService {

    @POST("produto")
    Call<Item> insert(@Body Item item);
}
