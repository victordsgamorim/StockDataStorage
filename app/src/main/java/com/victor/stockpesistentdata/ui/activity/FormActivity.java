package com.victor.stockpesistentdata.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.victor.stockpesistentdata.R;
import com.victor.stockpesistentdata.model.Item;
import com.victor.stockpesistentdata.util.PriceUtil;
import com.victor.stockpesistentdata.util.QuantityUtil;

import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_DATA;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_EDIT;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.ITEM_RESULT;
import static com.victor.stockpesistentdata.ui.constant.TransferDadaBetweenIntentConstant.POSITION;

public class FormActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText priceField;
    private EditText qntField;
    private Item internalItem;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        System.out.println("Result");
        initFieldViews();

        Intent data = getIntent();
        if (data.hasExtra(ITEM_EDIT) && data.hasExtra(POSITION)) {
            internalItem = (Item) data.getSerializableExtra(ITEM_EDIT);
            position = data.getIntExtra(POSITION, -1);

            fillFieldWithExistingItemData();
        }


    }

    private void fillFieldWithExistingItemData() {
        nameField.setText(internalItem.getName());
        priceField.setText(PriceUtil.toString(internalItem.getPrice()));
        qntField.setText(QuantityUtil.toString(internalItem.getQuantity()));
    }

    private void initFieldViews() {
        nameField = findViewById(R.id.activity_form_name);
        priceField = findViewById(R.id.activity_form_price);
        qntField = findViewById(R.id.activity_form_quantity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_done_add_item) {


            Item itemCreated = getItemInfoFromField();
            Intent intent = new Intent();

            if (internalItem == null) {
                intent.putExtra(ITEM_DATA, itemCreated);
                setResult(ITEM_RESULT, intent);
            } else {
                itemCreated.setId(internalItem.getId());
                intent.putExtra(ITEM_EDIT, itemCreated);
                intent.putExtra(POSITION, position);
                setResult(ITEM_RESULT, intent);
            }


            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private Item getItemInfoFromField() {
        String name = nameField.getText().toString();
        String price = priceField.getText().toString();
        String quantity = qntField.getText().toString();

        return new Item(name, PriceUtil.toBigDecimal(price), QuantityUtil.toInteger(quantity));
    }
}
