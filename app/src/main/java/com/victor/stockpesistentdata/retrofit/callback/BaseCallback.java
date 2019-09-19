package com.victor.stockpesistentdata.retrofit.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class BaseCallback<T> implements Callback<T> {

    private static final String RESPONSE_ERROR = "Error after trying to get the Response";
    private static final String API_ERROR = "Error after trying to connect to API";
    private final DataCallbackListener<T> listener;

    public BaseCallback(DataCallbackListener<T> listener) {
        this.listener = listener;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            T result = response.body();
            if (result != null) {

                listener.onSuccess(result);
            }
        } else {
            listener.onFailure(RESPONSE_ERROR);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<T> call, Throwable t) {
        listener.onFailure(API_ERROR + " " + t.getMessage());

    }

    public interface DataCallbackListener<T> {
        void onSuccess(T result);

        void onFailure(String error);
    }
}
