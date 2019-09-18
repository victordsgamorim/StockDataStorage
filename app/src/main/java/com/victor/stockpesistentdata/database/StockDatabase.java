package com.victor.stockpesistentdata.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.victor.stockpesistentdata.database.converter.PriceConverter;
import com.victor.stockpesistentdata.database.dao.ItemDAO;
import com.victor.stockpesistentdata.model.Item;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
@TypeConverters({PriceConverter.class})
public abstract class StockDatabase extends RoomDatabase {

    public abstract ItemDAO getItemDAO();

    public static StockDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, StockDatabase.class, "itemstock.db")
                .build();
    }
}
