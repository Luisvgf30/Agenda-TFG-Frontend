package com.example.miagenda.api.retrofit;

import com.example.miagenda.api.Usuario;
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

    public interface UserFetchCallback {
        void onSuccess(Usuario user);
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

    public void buscarUsuario(String username, UserFetchCallback callback) {
        Call<Usuario> call = perfilAPI.buscarUser(username);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Usuario user = response.body();
                    if (user != null) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError("Usuario no encontrado");
                    }
                } else {
                    callback.onError("Código de error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }
}
