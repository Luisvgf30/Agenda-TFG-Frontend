package com.example.miagenda.api.retrofit;



import com.example.miagenda.api.Tarea;
import com.example.miagenda.api.Usuario;
import com.example.miagenda.api.UsuarioActualizarRequest;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PerfilAPI {
    @FormUrlEncoded
    @POST("/createTask")
    Call<Void> createTask(
            @Field("task_name")String task_name,
            @Field("task_desc")String task_desc,
            @Field("limit_date")LocalDate limit_date,
            @Field("username")String username
    );


    @FormUrlEncoded
    @POST("/singUpUsu") // dentro de las comillas la ruta en la api"registrase"
    Call<Usuario> registrarUsuario(
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password
                );



    @GET("/login")
    Call<Usuario> logearUsuario(
            @Query("username") String usuario,
            @Query("password") String password
    );

    @PUT("/editUsu")
    Call<Void> updateUser(
            @Query("username") String username,
            @Body UsuarioActualizarRequest request
    );

    @GET("/buscarUsu")
    Call<Usuario> buscarUser(
            @Query("username") String usuario
    );

    @FormUrlEncoded
    @POST("") // dentro de las comillas la ruta en la api"logout"
    Call<Usuario> Logout(
            @Field("token") String token
    );

    @POST("/createTask")
    Call<Void> createTask(
            @Query("task_name") String task_name,
            @Query("task_desc") String task_desc,
            @Query("state") String state,
            @Query("document") String document,
            @Query("limit_date") String limit_date,
            @Query("initial_date") String initial_date
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


