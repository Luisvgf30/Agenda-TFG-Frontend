package com.example.miagenda.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Tarea;
import com.example.miagenda.api.Usuario;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksFragment extends Fragment {

    private List<Tarea> tasks = new ArrayList<>();
    private LinearLayout noTasksContainer;
    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        FloatingActionButton fabAgregarTarea = view.findViewById(R.id.fabAgregarTarea);
        fabAgregarTarea.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.addTasks);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext()); // Inicializa el sessionManager con requireContext()

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        tasksAdapter = new TasksAdapter(new ArrayList<>(), sessionManager);
        recyclerView.setAdapter(tasksAdapter);
        noTasksContainer = view.findViewById(R.id.no_tasks_container);

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
                        tasks.clear();
                        tasks.addAll(response.body());
                        tasksAdapter.setTareas(tasks); // Actualizar el adaptador con las nuevas tareas
                        updateNoTasksView(); // Llamada para actualizar la vista aquí
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

    private void updateNoTasksView() {
        if (tasks.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noTasksContainer.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noTasksContainer.setVisibility(View.GONE);
        }
    }
}
