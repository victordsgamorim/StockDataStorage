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
                .baseUrl("https://4803c84d-02d1-44b0-814b-d25d31dbc327.mock.pstmn.io/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ItemService.class);
    }

    public ItemService getItemService() {
        return service;
    }
}
