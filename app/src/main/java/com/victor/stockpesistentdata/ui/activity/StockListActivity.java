package com.victor.stockpesistentdata.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.victor.stockpesistentdata.R;
import com.victor.stockpesistentdata.model.Item;
import com.victor.stockpesistentdata.repository.ItemRepository;
import com.victor.stockpesistentdata.ui.adapter.StockListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_DATA;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_EDIT;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_REQUEST;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_RESULT;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.POSITION;

public class StockListActivity extends AppCompatActivity {

    private StockListAdapter adapter;
    private ItemRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        /**instace of internal and external database*/
        repository = new ItemRepository(this);

        configRecyclerView();
        fabAddItemListener();
        adapterListener();

        /**thread async that search for item on internal db*/
        readItems();
    }

    private void readItems() {
        repository.read(new ItemRepository.DataManipulationAfterAsyncBackgroundFinish<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                adapter.updateList(result);
            }

            @Override
            public void onFailure(String error) {
                Log.e("onRead", error );
            }
        });
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
            repository.delete(item, new ItemRepository.DataManipulationAfterAsyncBackgroundFinish<Void>() {
                @Override
                public void onSuccess(Void result) {
                    adapter.delete(item);
                }

                @Override
                public void onFailure(String error) {
                    Log.e("onDelete", error );
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**insert item into internal and external storage*/
        if (requestCode == ITEM_REQUEST
                && resultCode == ITEM_RESULT
                && data.hasExtra(ITEM_DATA)) {
            add(data);

        }

        /**edit item from internal and external storage*/
        if (requestCode == ITEM_REQUEST
                && resultCode == ITEM_RESULT
                && data.hasExtra(ITEM_EDIT)
                && data.hasExtra(POSITION)) {

            update(data);

        }
    }

    private void update(@NotNull Intent data) {
        Item item = (Item) data.getSerializableExtra(ITEM_EDIT);
        int position = data.getIntExtra(POSITION, -1);
        repository.update(item, new ItemRepository.DataManipulationAfterAsyncBackgroundFinish<Item>() {
            @Override
            public void onSuccess(Item result) {
                adapter.update(position, result);
            }

            @Override
            public void onFailure(String error) {
                Log.e("onUpdate", error );
            }
        });
    }

    private void add(@NotNull Intent data) {
        Item item = (Item) data.getSerializableExtra(ITEM_DATA);
        repository.create(item, new ItemRepository.DataManipulationAfterAsyncBackgroundFinish<Item>() {
            @Override
            public void onSuccess(Item result) {
                adapter.add(result);
            }

            @Override
            public void onFailure(String error) {
                Log.e("onAdd", error );
            }
        });
    }

}
