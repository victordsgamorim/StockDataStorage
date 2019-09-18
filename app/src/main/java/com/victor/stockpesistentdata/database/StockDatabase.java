package com.victor.stockpesistentdata.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.victor.stockpesistentdata.database.converter.PriceConverter;
import com.victor.stockpesistentdata.database.dao.ItemDAO;
import com.victor.stockpesistentdata.model.Item;

@Database(entities = {Item.class}, version = 2, exportSchema = false)
@TypeConverters({PriceConverter.class})
public abstract class StockDatabase extends RoomDatabase {

    public abstract ItemDAO getItemDAO();

    public static StockDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, StockDatabase.class, "itemstock.db")
                .addMigrations(new Migration(1, 2) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("CREATE TABLE IF NOT EXISTS `Item_new` (" +
                                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                "`name` TEXT, " +
                                "`price` REAL, `" +
                                "quantity` INTEGER NOT NULL)");

                        database.execSQL("INSERT INTO Item_new (name, price, quantity) " +
                                "SELECT name, price, quantity FROM Item");

                        database.execSQL("DROP TABLE IF EXISTS Item");
                        database.execSQL("ALTER TABLE Item_new RENAME TO Item");
                    }
                })
                .build();
    }
}
