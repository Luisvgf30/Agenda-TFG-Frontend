package com.example.miagenda.api.retrofit;



import com.example.miagenda.api.Usuario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PerfilAPI {
    @FormUrlEncoded
    @POST("/singUpUsu") // dentro de las comillas la ruta en la api"registrase"
    Call<Usuario> RegistrarUsuario(
            @Field("email") String email,
            @Field("usuario") String usuario,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password
    );


    @FormUrlEncoded
    @POST("/login") // dentro de las comillas la ruta en la api"lohgearse"
    Call<Usuario> LogearUsuario(
            @Field("usuario") String usuario,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("") // dentro de las comillas la ruta en la api"logout"
    Call<Usuario> Logout(
            @Field("token") String token
    );


}
