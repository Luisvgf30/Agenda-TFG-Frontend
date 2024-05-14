package com.example.miagenda.api.retrofit;

import com.example.miagenda.api.Tarea;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TareasManager {

    private static TareasManager instance;
    private PerfilAPI perfilAPI;

    private TareasManager() {
        perfilAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);
    }

    public static synchronized TareasManager getInstance() {
        if (instance == null) {
            instance = new TareasManager();
        }
        return instance;
    }


}
