package com.victor.stockpesistentdata.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.victor.stockpesistentdata.model.Item;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    void insert(Item item);

    @Query("SELECT * FROM Item")
    List<Item> list();

    @Query("SELECT * FROM Item WHERE id = :id")
    Item searchItem(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(Item item);

    @Delete
    void delete(Item item);
}
