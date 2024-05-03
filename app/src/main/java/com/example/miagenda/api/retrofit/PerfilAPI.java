package com.example.miagenda.api.retrofit;



import com.example.miagenda.api.Usuario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PerfilAPI {
    @FormUrlEncoded
    @POST("/singUpUsu") // dentro de las comillas la ruta en la api"registrase"
    Call<Usuario> RegistrarUsuario(
            @Field("nombre") String nombre,
            @Field("apellido") String apellido,
            @Field("email") String email,
            @Field("usuario") String usuario,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password
    );



    @GET("/login") // Especifica la ruta relativa de tu API para el login
    Call<Usuario> logearUsuario(
            @Query("usuario") String usuario,
            @Query("password") String password
    );

    @FormUrlEncoded
    @POST("") // dentro de las comillas la ruta en la api"logout"
    Call<Usuario> Logout(
            @Field("token") String token
    );


}
