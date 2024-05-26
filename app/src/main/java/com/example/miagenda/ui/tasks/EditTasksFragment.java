// EditTasksFragment.java
package com.example.miagenda.ui.tasks;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTasksFragment extends Fragment {

    private EditText editNombreTarea;
    private EditText editDescripcionTarea;
    private EditText editFechaLimiteTarea;
    private Spinner editEstadoTarea;
    private Spinner editPrioridadTarea;
    private String oldTaskName;
    private LocalDate selectedDate;
    private Context context;
    private String oldStartDate; // Variable para almacenar la fecha inicial

    public EditTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oldTaskName = getArguments().getString("taskName");
            oldStartDate = getArguments().getString("startDate"); // Obtener la fecha inicial
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        if (context == null) {
            return;
        }

        editNombreTarea = view.findViewById(R.id.editNombreTarea);
        editDescripcionTarea = view.findViewById(R.id.editDescripcionTarea);
        editFechaLimiteTarea = view.findViewById(R.id.editFechaLimiteTarea);
        editEstadoTarea = view.findViewById(R.id.editEstadoTarea);
        editPrioridadTarea = view.findViewById(R.id.editPrioridadTarea);

        ImageButton botonAtras = view.findViewById(R.id.boton_atras);
        botonAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        Button editarTareaButton = view.findViewById(R.id.editarTareaButton);
        editarTareaButton.setOnClickListener(v -> editarTarea());

        // Configurar el Spinner de Estado
        ArrayAdapter<CharSequence> estadoAdapter = ArrayAdapter.createFromResource(context,
                R.array.task_states, android.R.layout.simple_spinner_item);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editEstadoTarea.setAdapter(estadoAdapter);

        // Configurar el Spinner de Prioridad
        ArrayAdapter<CharSequence> prioridadAdapter = ArrayAdapter.createFromResource(context,
                R.array.task_priority, android.R.layout.simple_spinner_item);
        prioridadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editPrioridadTarea.setAdapter(prioridadAdapter);

        // Llenar los campos con los datos pasados
        if (getArguments() != null) {
            String taskName = getArguments().getString("taskName");
            String taskDesc = getArguments().getString("taskDesc");
            String taskStatus = getArguments().getString("status");
            String taskPriority = getArguments().getString("priority");

            editNombreTarea.setText(taskName);
            editDescripcionTarea.setText(taskDesc);

            if (taskStatus != null) {
                int statusPosition = estadoAdapter.getPosition(taskStatus);
                editEstadoTarea.setSelection(statusPosition);
            }

            if (taskPriority != null) {
                int priorityPosition = prioridadAdapter.getPosition(taskPriority);
                editPrioridadTarea.setSelection(priorityPosition);
            }
        }

        editFechaLimiteTarea.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Selecciona una fecha");
        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.ofEpochDay(selection / (24 * 60 * 60 * 1000));
            }
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                editFechaLimiteTarea.setText(selectedDate.format(formatter));
            }
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void editarTarea() {
        String newTaskName = editNombreTarea.getText().toString().trim();
        String newTaskDesc = editDescripcionTarea.getText().toString().trim();
        String newLimitDateStr = editFechaLimiteTarea.getText().toString().trim();
        String newEstado = editEstadoTarea.getSelectedItem().toString();
        String newTaskLevel = editPrioridadTarea.getSelectedItem().toString();

        if (newTaskName.isEmpty() || newTaskDesc.isEmpty() || newLimitDateStr.isEmpty() || newEstado.isEmpty() || newTaskLevel.isEmpty()) {
            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir la fecha límite de string a LocalDate
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
        }
        LocalDate newLimitDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            newLimitDate = LocalDate.parse(newLimitDateStr, formatter);
        }

        // Convertir LocalDate a string en formato "yyyy-MM-dd"
        String newLimitDateFormatted = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            newLimitDateFormatted = newLimitDate.toString();
        }

        PerfilAPI perfilAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);
        SessionManager sessionManager = new SessionManager(context);
        String username = sessionManager.getUser().getUsername();

        Call<Void> call = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            call = perfilAPI.editTask(
                    username,
                    oldTaskName,
                    newTaskName,
                    newTaskDesc,
                    LocalDate.parse(newLimitDateFormatted), // Utiliza el formato correcto de fecha
                    newEstado,
                    newTaskLevel
            );
        }
        String finalNewLimitDateFormatted = newLimitDateFormatted;
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("taskName", newTaskName);
                    bundle.putString("taskDesc", newTaskDesc);
                    bundle.putString("dueDate", finalNewLimitDateFormatted); // Utiliza el formato correcto de fecha
                    bundle.putString("startDate", oldStartDate); // Agrega la fecha inicial aquí
                    bundle.putString("status", newEstado);
                    bundle.putString("priority", newTaskLevel);

                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.editTasks, true)  // Limpia la pila de retroceso hasta este fragmento
                            .build();

                    NavHostFragment.findNavController(EditTasksFragment.this)
                            .navigate(R.id.navigation_tasks, bundle, navOptions);
                } else {
                    Toast.makeText(context, "Error al actualizar la tarea", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
