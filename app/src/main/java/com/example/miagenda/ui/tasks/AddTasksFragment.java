package com.example.miagenda.ui.tasks;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Tarea;
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

    private EditText addNombreTarea, addDescripcionTarea, addFechaInicial, addFechaLimite, addEstado, addPrioridad, addDocumento;
    private EditText editTaskName, editTaskDesc, fechaLimite;
    private SessionManager sessionManager;

    public AddTasksFragment() {
        // Required empty public constructor
    }

    public static AddTasksFragment newInstance(String param1, String param2) {
        AddTasksFragment fragment = new AddTasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backButton = view.findViewById(R.id.boton_atras);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        Button addTaskButton = view.findViewById(R.id.addTareaButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
            }
        });

        // Initialize SessionManager
        sessionManager = new SessionManager(requireContext());

        // Find all EditText fields
        addNombreTarea = view.findViewById(R.id.addNombreTarea);
        addDescripcionTarea = view.findViewById(R.id.addDescripcionTarea);
        addFechaInicial = view.findViewById(R.id.addFechaInicialTarea);
        addFechaLimite = view.findViewById(R.id.addFechaLimiteTarea);
        addEstado = view.findViewById(R.id.addEstadoTarea);
        addPrioridad = view.findViewById(R.id.addPrioridadTarea);
        addDocumento = view.findViewById(R.id.addDocumentoTarea);
        fechaLimite = view.findViewById(R.id.fechaLimite);
        editTaskName = view.findViewById(R.id.addTareaName);
        editTaskDesc = view.findViewById(R.id.addDescTarea);
    }

    private void createTask() {
        String username = sessionManager.getUser().getUsername(); // Get username from SessionManager
        if (username == null) {
            Toast.makeText(requireContext(), "Usuario no logeado", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskName = editTaskName.getText().toString();
        String taskDesc = editTaskDesc.getText().toString();
        String limitDateStr = fechaLimite.getText().toString();

        if (taskName.isEmpty() || taskDesc.isEmpty() || limitDateStr.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate limitDate = parseDate(limitDateStr);

        if (limitDate != null) {
            Tarea nuevaTarea = new Tarea(taskName, taskDesc, limitDate, username); // Pass username to Tarea constructor
        String userName = addNombreTarea.getText().toString();
        String email = addDescripcionTarea.getText().toString();
        String initialDateStr = addFechaInicial.getText().toString();
        String limitDateStr = addFechaLimite.getText().toString();
        String estado = addEstado.getText().toString();
        String prioridad = addPrioridad.getText().toString();
        String documento = addDocumento.getText().toString();

            Log.d(TAG, "Nueva tarea creada:--------------------------------------------------------------------------------------------------------------------------------------------------- " + nuevaTarea);

            PerfilAPI apiService = RetrofitCliente.getInstance().create(PerfilAPI.class);
            Call<Void> call = apiService.createTask(taskName, taskDesc, limitDate, username);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Tarea creada exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Error al crear la tarea: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireContext(), "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
        }
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

