# StockDataStorage
---

### This project aims the study of Android Application using the best practices. 

First of all, it consist of a list of items that can be added, updated, read and removed using concepts of design pattern as DAO(Data Access Object) and Object Orientation. 

## Dependecies

1. Recyclerview
2. Cardview 
3. Room
4. Retrofit
5. Converter-Gson
6. OkHttp

## Note: Retrofit Service

Retrofit base url uses the one created by postman mock server, in case of running in different computers, it will not work. 

1. First change the base Url and insert the one that fit better to the project.
```java

retrofit = new Retrofit.Builder()
                .baseUrl("https://4803c84d-02d1-44b0-814b-d25d31dbc327.mock.pstmn.io/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
```

2. In order to retrieve the data from the server, it is also needed to change the request.

```java

    @POST("stock")
    Call<Item> insert(@Body Item item);

    @PUT("stock/diff")
    Call<Item> update(@Header("item") Item item);

    @GET("stock")
    Call<List<Item>> list();

    @DELETE("stock/{id}")
    Call<Void> delete(@Path("id") int id);
```

