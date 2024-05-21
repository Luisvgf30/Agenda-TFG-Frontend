package com.example.miagenda.ui.tasks;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.miagenda.R;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddTasksFragment extends Fragment {

    private EditText addNombreTarea, addDescripcionTarea, addFechaInicial, addFechaLimite, addEstado, addPrioridad, addDocumento;

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

        // Find all EditText fields
        addNombreTarea = view.findViewById(R.id.addNombreTarea);
        addDescripcionTarea = view.findViewById(R.id.addDescripcionTarea);
        addFechaInicial = view.findViewById(R.id.addFechaInicialTarea);
        addFechaLimite = view.findViewById(R.id.addFechaLimiteTarea);
        addEstado = view.findViewById(R.id.addEstadoTarea);
        addPrioridad = view.findViewById(R.id.addPrioridadTarea);

    }

    private void createTask() {
        String userName = addNombreTarea.getText().toString();
        String email = addDescripcionTarea.getText().toString();
        String initialDateStr = addFechaInicial.getText().toString();
        String limitDateStr = addFechaLimite.getText().toString();
        String estado = addEstado.getText().toString();
        String prioridad = addPrioridad.getText().toString();
        String documento = addDocumento.getText().toString();

        // Check if dates are in the correct format
        Date initialDate = parseDate(initialDateStr);
        Date limitDate = parseDate(limitDateStr);

        // Make sure the date parsing was successful
        if (initialDate != null && limitDate != null) {
            // Call your API to create the task with the parsed dates
            // Note: You'll need to implement your Retrofit call here
            // For example:
            // createTask(userName, email, initialDate, limitDate, estado, prioridad, documento);
            Toast.makeText(requireContext(), "Tarea creada exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            java.util.Date parsedDate = dateFormat.parse(dateString);
            if (parsedDate != null) {
                return new Date(parsedDate.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

