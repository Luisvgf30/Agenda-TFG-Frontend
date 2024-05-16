package com.example.miagenda.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Tarea;
import com.example.miagenda.api.Usuario;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        tasksAdapter = new TasksAdapter(new ArrayList<>());
        recyclerView.setAdapter(tasksAdapter);

        loadUserTasks();
    }

    private void loadUserTasks() {
        Usuario user = sessionManager.getUser();
        if (user != null) {
            String username = user.getUsername();
            PerfilAPI perfilAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);
            Call<List<Tarea>> call = perfilAPI.buscarTasks(username);
            call.enqueue(new Callback<List<Tarea>>() {
                @Override
                public void onResponse(Call<List<Tarea>> call, Response<List<Tarea>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Tarea> tareas = response.body();
                        tasksAdapter.setTareas(tareas); // Actualizar el adaptador con las nuevas tareas
                    } else {
                        Log.e("TasksFragment", "Error en la respuesta: " + response.message());
                    }
                }


                @Override
                public void onFailure(Call<List<Tarea>> call, Throwable t) {
                    Log.e("TasksFragment", "Error en la solicitud: " + t.getMessage());
                }
            });
        }
    }
}
