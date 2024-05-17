package com.example.miagenda.ui.tasks;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTasksFragment extends Fragment {


    private EditText editNombreTarea;
    private EditText editDescripcionTarea;
    private EditText editFechaInicialTarea;
    private EditText editFechaLimiteTarea;
    private EditText editEstadoTarea;
    private EditText editPrioridadTarea;
    private String oldTaskName;


    public EditTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oldTaskName = getArguments().getString("taskName");
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

        editNombreTarea = view.findViewById(R.id.editNombreTarea);
        editDescripcionTarea = view.findViewById(R.id.editDescripcionTarea);
        editFechaInicialTarea = view.findViewById(R.id.editFechaInicialTarea);
        editFechaLimiteTarea = view.findViewById(R.id.editFechaLimiteTarea);
        editEstadoTarea = view.findViewById(R.id.editEstadoTarea);
        editPrioridadTarea = view.findViewById(R.id.editPrioridadTarea);

        ImageButton botonAtras = view.findViewById(R.id.boton_atras);
        botonAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        Button editarTareaButton = view.findViewById(R.id.editarTareaButton);
        editarTareaButton.setOnClickListener(v -> editarTarea());
    }

    private void editarTarea() {
        String newTaskName = editNombreTarea.getText().toString().trim();
        String newTaskDesc = editDescripcionTarea.getText().toString().trim();
        String newLimitDate = editFechaLimiteTarea.getText().toString().trim();
        String newEstado = editEstadoTarea.getText().toString().trim();
        String newTaskLevel = editPrioridadTarea.getText().toString().trim();

        // Verificar que los campos obligatorios no estén vacíos
        if (newTaskName.isEmpty() || newTaskDesc.isEmpty() || newLimitDate.isEmpty() || newEstado.isEmpty() || newTaskLevel.isEmpty()) {
            // Mostrar un mensaje de error o hacer algo para indicar al usuario que complete todos los campos
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear la instancia de Retrofit y la interfaz API
        PerfilAPI perfilAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);

        SessionManager sessionManager = new SessionManager(requireContext());
        String username = sessionManager.getUser().getUsername();
        // Hacer la solicitud para editar la tarea
        Call<Void> call = perfilAPI.editTask(username, oldTaskName, newTaskName, newTaskDesc, newLimitDate, newEstado, newTaskLevel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Preparar el bundle con los datos actualizados
                    Bundle bundle = new Bundle();
                    bundle.putString("taskName", newTaskName);
                    bundle.putString("taskDesc", newTaskDesc);
                    bundle.putString("dueDate", newLimitDate);
                    bundle.putString("status", newEstado);
                    bundle.putString("priority", newTaskLevel);

                    // Navegar de vuelta al fragmento de MyTask y pasar los datos actualizados
                    Navigation.findNavController(getView()).navigate(R.id.action_editTasksFragment_to_myTaskFragment, bundle);
                } else {
                    // Manejar error
                    Toast.makeText(getContext(), "Error al actualizar la tarea", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Manejar fallo de la red
                Toast.makeText(getContext(), "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
