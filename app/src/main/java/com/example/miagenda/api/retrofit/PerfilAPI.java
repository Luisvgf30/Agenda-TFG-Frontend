package com.example.miagenda.api.retrofit;



import com.example.miagenda.api.Evento;
import com.example.miagenda.api.Nota;
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


	/*@FormUrlEncoded
	@POST("") // dentro de las comillas la ruta en la api"logout"
    Call<Usuario> Logout(
            @Field("token") String token
    );*/




	@DELETE("/deleteTask")
    Call<Void> deleteTask(
            @Query("username") String username,
            @Query("task_name") String taskName
    );


	@GET("/buscarTasks")
    Call<List<Tarea>> buscarTasks(
            @Query("username") String username
    );


	@FormUrlEncoded
	@POST("/createTask")
    Call<Void> createTask(
            @Field("task_name")String task_name,
            @Field("task_desc")String task_desc,
            @Field("limit_date")LocalDate limit_date,
            @Field("task_level")String task_level,
            @Field("username")String username
    );


	@FormUrlEncoded
	@PUT("/editTask")
    Call<Void> editTask(
            @Field("username") String username,
            @Field("old_task_name") String oldTaskName,
            @Field("new_task_name") String newTaskName,
            @Field("new_task_desc") String newTaskDesc,
            @Field("new_limit_date") LocalDate newLimitDate,
            @Field("new_estado") String newEstado,
            @Field("new_task_level") String newTaskLevel
    );

	@DELETE("/deleteNote")
    Call<Void> deleteNote(
            @Query("username") String username,
            @Query("id") String id
    );


	@GET("/buscarNotes")
    Call<List<Nota>> buscarNotes(@Query("username") String username);


	@FormUrlEncoded
	@POST("/createNote")
    Call<Void> createNote(
            @Field("note_desc") String note_desc,
            @Field("username") String username);


	@FormUrlEncoded
	@POST("/createEvent")
    Call<Void> createEvent(
            @Field("username") String username,
            @Field("event_name") String eventName,
            @Field("event_desc") String eventDesc,
            @Field("event_date") LocalDate eventDate
    );


	@DELETE("/deleteEvent")
    Call<Void> deleteEvent(
            @Query("username") String username,
            @Query("event_name") String eventName
    );


	@GET("/buscarEvents")
    Call<List<Evento>> buscarEvents(
            @Query("username") String username
    );


	@FormUrlEncoded
	@PUT("/editEvent")
    Call<Void> editEvent(
            @Field("username") String username,
            @Field("old_event_name") String oldEventName,
            @Field("new_event_name") String newEventName,
            @Field("new_event_desc") String newEventDesc,
            @Field("new_event_date") LocalDate newEventDate
    );

}


