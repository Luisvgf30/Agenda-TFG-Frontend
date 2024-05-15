package com.example.miagenda.api.retrofit;

import com.example.miagenda.api.UsuarioActualizarRequest;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioApiCliente {

    public interface UserUpdateCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    private PerfilAPI perfilAPI;

    public UsuarioApiCliente() {
        this.perfilAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);
    }

    public void updateUser(String username, UsuarioActualizarRequest updateRequest, UserUpdateCallback callback) {
        Call<Void> call = perfilAPI.updateUser(username, updateRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(); // Llamada exitosa
                } else {
                    callback.onError("Código de error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }
}
