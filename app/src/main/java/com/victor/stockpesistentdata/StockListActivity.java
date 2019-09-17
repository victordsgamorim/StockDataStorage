package com.victor.stockpesistentdata;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.FloatArrayEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLOutput;

public class StockListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        FloatingActionButton fab = findViewById(R.id.activity_fab_add_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO open form activity in order to add item
                Intent intent = new Intent(StockListActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });


    }
}
