package com.example.miagenda.api.retrofit;



import com.example.miagenda.api.Tarea;
import com.example.miagenda.api.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PerfilAPI {
    @FormUrlEncoded
    @POST("/singUpUsu") // dentro de las comillas la ruta en la api"registrase"
    Call<Usuario> RegistrarUsuario(
            @Field("nombre") String nombre,
            @Field("apellido") String apellido,
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password
    );


    @GET("/login")
    Call<Usuario> logearUsuario(
            @Query("username") String usuario,
            @Query("password") String password
    );

    @FormUrlEncoded
    @POST("") // dentro de las comillas la ruta en la api"logout"
    Call<Usuario> Logout(
            @Field("token") String token
    );

    @POST("/createTask")
    Call<Void> createTask(
            @Query("username") String username,
            @Query("task_name") String taskName,
            @Query("task_desc") String taskDesc,
            @Query("limit_date") String limitDate
    );

    @DELETE("/deleteTask")
    Call<Void> deleteTask(
            @Query("username") String username,
            @Query("task_name") String taskName
    );

    @GET("/buscarTasks")
    Call<List<Tarea>> buscarTasks(
            @Query("username") String username
    );

}


