package com.example.miagenda.ui.tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTaskFragment extends Fragment {

    private String taskName;
    private String taskDesc;
    private String startDate;
    private String dueDate;
    private String status;
    private String level;
    private SessionManager sessionManager;

    public MyTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskName = getArguments().getString("taskName");
            taskDesc = getArguments().getString("taskDesc");
            startDate = getArguments().getString("startDate");
            dueDate = getArguments().getString("dueDate");
            status = getArguments().getString("status");
            level = getArguments().getString("level");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getContext()); // Inicializar sessionManager

        TextView nombreTarea = view.findViewById(R.id.nombreTarea);
        TextView descripcionTarea = view.findViewById(R.id.descripcionTarea);
        TextView fechaInicialTarea = view.findViewById(R.id.fechaInicialTarea);
        TextView fechaLimiteTarea = view.findViewById(R.id.fechaLimiteTarea);
        TextView estadoTarea = view.findViewById(R.id.estadoTarea);
        TextView levelTarea = view.findViewById(R.id.levelTarea);

        nombreTarea.setText(taskName);
        descripcionTarea.setText(taskDesc);
        fechaInicialTarea.setText(startDate);
        fechaLimiteTarea.setText(dueDate);
        estadoTarea.setText(status);
        levelTarea.setText(level);  // Asignar el nivel a la vista
        Log.d("MyTaskFragment", "Level in onViewCreated: " + level);

        view.findViewById(R.id.boton_atras).setOnClickListener(v -> {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.myTask, true)
                    .build();

            NavHostFragment.findNavController(MyTaskFragment.this)
                    .navigate(R.id.navigation_tasks, null, navOptions);
        });

        view.findViewById(R.id.miTareaButton).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("taskName", taskName);
            bundle.putString("taskDesc", taskDesc);
            bundle.putString("startDate", startDate);
            bundle.putString("dueDate", dueDate);
            bundle.putString("status", status);
            bundle.putString("level", level);
            Navigation.findNavController(v).navigate(R.id.action_myTaskFragment_to_editTasksFragment, bundle);
        });

        view.findViewById(R.id.deleteTaskButton).setOnClickListener(v -> deleteTask());
    }

    private void deleteTask() {
        // Realizar la llamada a la API para eliminar la tarea
        String username = sessionManager.getUsername();

        PerfilAPI apiService = RetrofitCliente.getInstance().create(PerfilAPI.class);
        Call<Void> call = apiService.deleteTask(username, taskName);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Tarea eliminada correctamente", Toast.LENGTH_SHORT).show();
                    // Navegar de vuelta a la lista de tareas
                    NavOptions navOptions
                            = new NavOptions.Builder()
                            .setPopUpTo(R.id.myTask, true) // Limpia la pila de retroceso hasta este fragmento
                            .build();
                    NavHostFragment.findNavController(MyTaskFragment.this)
                            .navigate(R.id.navigation_tasks, null, navOptions);
                } else {
                    Toast.makeText(getContext(), "Error al eliminar la tarea", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
