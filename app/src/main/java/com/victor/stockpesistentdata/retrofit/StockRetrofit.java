package com.victor.stockpesistentdata.retrofit;

import com.victor.stockpesistentdata.retrofit.service.ItemService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockRetrofit {

    private final Retrofit retrofit;
    private final ItemService service;

    public StockRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.73:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ItemService.class);
    }

    public ItemService getItemService() {
        return service;
    }
}
