package com.victor.stockpesistentdata.repository;

import android.content.Context;

import com.victor.stockpesistentdata.asynctask.BaseAsyncTask;
import com.victor.stockpesistentdata.database.StockDatabase;
import com.victor.stockpesistentdata.database.dao.ItemDAO;
import com.victor.stockpesistentdata.model.Item;
import com.victor.stockpesistentdata.retrofit.StockRetrofit;
import com.victor.stockpesistentdata.retrofit.callback.BaseCallback;
import com.victor.stockpesistentdata.retrofit.service.ItemService;

import java.util.List;

import retrofit2.Call;

public class ItemRepository {

    private final ItemDAO dao;
    private final ItemService service;

    public ItemRepository(Context context) {
        StockDatabase database = StockDatabase.getInstance(context);
        service = new StockRetrofit().getItemService();
        dao = database.getItemDAO();
    }

    public void create(Item item,
                       DataManipulationAfterAsyncBackgroundFinish<Item> callback) {

        new BaseAsyncTask<>(() -> {
            dao.insert(item);
            return dao.searchItem(item.getId());

        }, result -> {
            callback.onSuccess(result);
            addItemApi(result, callback);
        }).execute();
    }

    private void addItemApi(Item result, DataManipulationAfterAsyncBackgroundFinish<Item> callback) {
        Call<Item> call = service.insert(result);

        call.enqueue(new BaseCallback<>(new BaseCallback.DataCallbackListener<Item>() {
            @Override
            public void onSuccess(Item result) {
                //TODO SOMETHING WITH THE RESULT
                // something related to sync and add new item
                // callback.onSuccess(result);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        }));
    }

    public void read(DataManipulationAfterAsyncBackgroundFinish<List<Item>> callback) {
        new BaseAsyncTask<>(dao::list, result -> {
            callback.onSuccess(result);
            getItemApi(callback);
        }).execute();
    }

    private void getItemApi(DataManipulationAfterAsyncBackgroundFinish<List<Item>> callback) {
        Call<List<Item>> call = service.list();
        call.enqueue(new BaseCallback<>(new BaseCallback.DataCallbackListener<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                //TODO SOMETHING WITH THE RESULT
                // something related to sync and get new item
                // callback.onSuccess(result);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        }));
    }


    public void update(Item item, DataManipulationAfterAsyncBackgroundFinish<Item> callback) {
        new BaseAsyncTask<>(() -> {
            dao.update(item);
            return dao.searchItem(item.getId());
        }, result -> {
            callback.onSuccess(result);
            updateItemApi(item, callback);
        }).execute();
    }

    private void updateItemApi(Item item, DataManipulationAfterAsyncBackgroundFinish<Item> callback) {
        Call<Item> call = service.update(item);
        call.enqueue(new BaseCallback<>(new BaseCallback.DataCallbackListener<Item>() {
            @Override
            public void onSuccess(Item result) {
                //TODO SOMETHING WITH THE RESULT
                // something related to sync and update item
                // callback.onSuccess(result);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        }));
    }

    public void delete(Item item, DataManipulationAfterAsyncBackgroundFinish<Void> callback) {
        new BaseAsyncTask<>(() -> {
            dao.delete(item);
            return null;
        }, result -> {
            callback.onSuccess((Void) result);
            deleteItemApi(item, callback);
        }).execute();
    }

    private void deleteItemApi(Item item, DataManipulationAfterAsyncBackgroundFinish<Void> callback) {
        Call<Void> delete = service.delete(item.getId());
        delete.enqueue(new BaseCallback<>(new BaseCallback.DataCallbackListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                //TODO SOMETHING WITH THE RESULT
                // something related to sync and delete item
                // callback.onSuccess(result);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        }));
    }

    public interface DataManipulationAfterAsyncBackgroundFinish<T> {
        void onSuccess(T result);

        void onFailure(String error);
    }

    public interface MessageWhenResponseIsOk {
        void onMessage(String message);
    }

}
