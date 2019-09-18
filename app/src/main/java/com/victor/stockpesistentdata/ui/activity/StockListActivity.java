package com.victor.stockpesistentdata.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.victor.stockpesistentdata.R;
import com.victor.stockpesistentdata.asynctask.BaseAsyncTask;
import com.victor.stockpesistentdata.database.StockDatabase;
import com.victor.stockpesistentdata.database.dao.ItemDAO;
import com.victor.stockpesistentdata.model.Item;
import com.victor.stockpesistentdata.retrofit.StockRetrofit;
import com.victor.stockpesistentdata.retrofit.service.ItemService;
import com.victor.stockpesistentdata.ui.adapter.StockListAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_DATA;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_EDIT;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_REQUEST;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_RESULT;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.POSITION;

public class StockListActivity extends AppCompatActivity {

    private StockDatabase database;
    private ItemDAO dao;
    private StockListAdapter adapter;
    private ItemService itemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        /**instace of sqlite database*/
        database = StockDatabase.getInstance(this);
        dao = database.getItemDAO();

        itemService = new StockRetrofit().getItemService();

        /**thread async that search for item on internal db*/

        configRecyclerView();
        fabAddItemListener();
        adapterListener();

        new BaseAsyncTask<>(dao::list, adapter::updateList).execute();
    }

    private void configRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.activity_stocklist_recyclerview);
        adapter = new StockListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void fabAddItemListener() {
        /***fab*/
        FloatingActionButton fab = findViewById(R.id.activity_fab_add_item);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(StockListActivity.this, FormActivity.class);
            startActivityForResult(intent, ITEM_REQUEST);

        });
    }

    private void adapterListener() {
        adapterListenerEdit();
        adapterListenerDelete();
    }

    private void adapterListenerEdit() {
        adapter.setItemClickListener((item, position) -> {
            Intent intent = new Intent(this, FormActivity.class);
            intent.putExtra(ITEM_EDIT, item);
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, ITEM_REQUEST);
        });
    }

    private void adapterListenerDelete() {
        adapter.setItemRemoveListener((item, position) -> {

            new BaseAsyncTask<Void>(() -> {
                dao.delete(item);
                return null;
            }, result -> adapter.delete(item)).execute();

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**insert item into internal and external storage*/
        if (requestCode == ITEM_REQUEST
                && resultCode == ITEM_RESULT
                && data.hasExtra(ITEM_DATA)) {

            Item item = (Item) data.getSerializableExtra(ITEM_DATA);

            Call<Item> call = itemService.insert(item);
            call.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    if (response.isSuccessful()) {
                        Item item = response.body();
                        if (item != null) {
                            addItemInternally(item);
                        } else {
                            //TODO error after trying to add to Internal Storage
                        }
                    }
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {
                    //TODO Error after trying to connect server to add the item
                }
            });


        }

        /**adeit item from internal and external storage*/
        if (requestCode == ITEM_REQUEST
                && resultCode == ITEM_RESULT
                && data.hasExtra(ITEM_EDIT)
                && data.hasExtra(POSITION)) {

            Item item = (Item) data.getSerializableExtra(ITEM_EDIT);
            int position = data.getIntExtra(POSITION, -1);

            new BaseAsyncTask<>(() -> {
                dao.update(item);
                return dao.searchItem(item.getId());
            }, result -> adapter.update(position, result)).execute();

        }
    }

    private void addItemInternally(Item item) {
        new BaseAsyncTask<>(() -> {
            dao.insert(item);
            return dao.searchItem(item.getId());

        }, adapter::add).execute();
    }
}
