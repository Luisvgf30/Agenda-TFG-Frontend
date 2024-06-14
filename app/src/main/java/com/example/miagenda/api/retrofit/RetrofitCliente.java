package com.example.miagenda.api.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitCliente {
    private static Retrofit retrofit;
    //la url de la api en local
    // ip ibra: 192.168.18.15
    //ip ruben: 192.168.1.233
    //192.168.1.138
    private static final String BASE_URL = "http://192.168.18.15:3000";

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
