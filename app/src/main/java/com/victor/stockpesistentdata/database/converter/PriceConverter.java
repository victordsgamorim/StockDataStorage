package com.victor.stockpesistentdata.database.converter;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.math.MathContext;

public class PriceConverter {

    @TypeConverter
    public Double toDouble(BigDecimal value) {
        return value.doubleValue();
    }

    @TypeConverter
    public BigDecimal toBigDecimal(Double value) {
        return new BigDecimal(value, MathContext.DECIMAL64);
    }
}
