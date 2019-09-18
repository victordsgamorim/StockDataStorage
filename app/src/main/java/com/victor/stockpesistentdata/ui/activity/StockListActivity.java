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
import com.victor.stockpesistentdata.ui.adapter.StockListAdapter;

import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_DATA;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_EDIT;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_REQUEST;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_RESULT;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.POSITION;

public class StockListActivity extends AppCompatActivity {

    private StockDatabase database;
    private ItemDAO dao;
    private StockListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        /**instace of sqlite database*/
        database = StockDatabase.getInstance(this);
        dao = database.getItemDAO();

        /**thread async that search for item on internal db*/
        new BaseAsyncTask<>(dao::list, adapter::updateList).execute();

        configRecyclerView();
        fabAddItemListener();
        adapterListener();

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

        if (requestCode == ITEM_REQUEST
                && resultCode == ITEM_RESULT
                && data.hasExtra(ITEM_DATA)) {

            Item item = (Item) data.getSerializableExtra(ITEM_DATA);

            new BaseAsyncTask<>(() -> {
                dao.insert(item);
                return dao.searchItem(item.getId());

            }, adapter::add).execute();
        }

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
}
