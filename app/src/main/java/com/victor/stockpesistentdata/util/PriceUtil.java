package com.victor.stockpesistentdata.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class PriceUtil {

    public static String priceCurrency(BigDecimal bigDecimal) {
        NumberFormat currecyFormat = NumberFormat
                .getCurrencyInstance(new Locale("de", "DE"));
        return currecyFormat.format(bigDecimal);
    }

    public static String toString(BigDecimal bigDecimal) {
        return String.valueOf(bigDecimal);
    }

    public static BigDecimal toBigDecimal(String price) {
        return new BigDecimal(price);
    }
}
