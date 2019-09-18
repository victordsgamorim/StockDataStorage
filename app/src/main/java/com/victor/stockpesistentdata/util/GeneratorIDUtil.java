package com.victor.stockpesistentdata.util;

public class GeneratorIDUtil {

    private static int id = 0;

    public static int generateId(){
        id++;
        return id;
    }
}
