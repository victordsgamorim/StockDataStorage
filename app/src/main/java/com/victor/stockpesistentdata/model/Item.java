package com.victor.stockpesistentdata.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.victor.stockpesistentdata.util.GeneratorIDUtil;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Item implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private BigDecimal price;
    private int quantity;

    public Item(String name, BigDecimal price, int quantity) {
        this.id = GeneratorIDUtil.generateId();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuantityString() {
        return "Qnt.: " + this.quantity;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
