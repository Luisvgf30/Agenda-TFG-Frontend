package com.example.miagenda.ui.tasks;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddTasksFragment extends Fragment {

    private EditText editTaskName, editTaskDesc, fechaLimite, taskLevel;
    private SessionManager sessionManager;

    public AddTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backButton = view.findViewById(R.id.boton_atras);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        Button addTaskButton = view.findViewById(R.id.addTareaButton);
        addTaskButton.setOnClickListener(v -> createTask());

        sessionManager = new SessionManager(requireContext());

        fechaLimite = view.findViewById(R.id.addFechaLimiteTarea);
        editTaskName = view.findViewById(R.id.addNombreTarea);
        editTaskDesc = view.findViewById(R.id.addDescripcionTarea);
        taskLevel = view.findViewById(R.id.addEstadoTarea);
    }

    private void createTask() {
        String username = sessionManager.getUser().getUsername();
        if (username == null) {
            Toast.makeText(requireContext(), "Usuario no logeado", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskName = editTaskName.getText().toString();
        String taskDesc = editTaskDesc.getText().toString();
        String limitDateStr = fechaLimite.getText().toString();
        String LevelTask = taskLevel.getText().toString();

        if (taskName.isEmpty() || taskDesc.isEmpty() || limitDateStr.isEmpty() || LevelTask.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate limitDate = parseDate(limitDateStr);

        PerfilAPI apiService = RetrofitCliente.getInstance().create(PerfilAPI.class);
        Call<Void> call = apiService.createTask(taskName, taskDesc, limitDate, LevelTask, username);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Tarea creada exitosamente", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Error al crear la tarea: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate parseDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
