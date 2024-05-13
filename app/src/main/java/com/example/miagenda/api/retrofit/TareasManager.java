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

    // Métodos para interactuar con la API
    /*public void crearTarea(String nombreTarea, String descripcion, String fechaInicial, String fechaLimite, String estado, String prioridad Callback<Void> callback) {
        Call<Void> call = perfilAPI.createTask(nombreTarea, descripcion, fechaInicial, fechaLimite, estado);
        call.enqueue(callback);
    }*/

    public void eliminarTarea(String username, String nombreTarea, Callback<Void> callback) {
        Call<Void> call = perfilAPI.deleteTask(username, nombreTarea);
        call.enqueue(callback);
    }

    public void buscarTareas(String username, Callback<List<Tarea>> callback) {
        Call<List<Tarea>> call = perfilAPI.buscarTasks(username);
        call.enqueue(callback);
    }
}
