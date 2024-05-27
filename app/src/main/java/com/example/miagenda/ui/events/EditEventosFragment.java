package com.example.miagenda.ui.events;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEventosFragment extends Fragment {

    private EditText editNombreEvento;
    private EditText editDescripcionEvento;
    private EditText editFechaEvento;
    private Button editarEventoButton;

    private String oldEventName;

    public EditEventosFragment() {
        // Required empty public constructor
    }

    public static EditEventosFragment newInstance(String oldEventName, String eventDesc, String eventDate) {
        EditEventosFragment fragment = new EditEventosFragment();
        Bundle args = new Bundle();
        args.putString("oldEventName", oldEventName);
        args.putString("eventDesc", eventDesc);
        args.putString("eventDate", eventDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oldEventName = getArguments().getString("oldEventName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_eventos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editNombreEvento = view.findViewById(R.id.editNombreEvento);
        editDescripcionEvento = view.findViewById(R.id.editDescripcionEvento);
        editFechaEvento = view.findViewById(R.id.editFechaEvento);
        editarEventoButton = view.findViewById(R.id.editarEventoButton);

        ImageButton botonAtras = view.findViewById(R.id.boton_atras);
        botonAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        if (getArguments() != null) {
            oldEventName = getArguments().getString("oldEventName");
            editNombreEvento.setText(oldEventName);
            editDescripcionEvento.setText(getArguments().getString("eventDesc"));
            editFechaEvento.setText(getArguments().getString("eventDate"));
        }

        // Listener para seleccionar la fecha
        editFechaEvento.setOnClickListener(v -> showDatePicker());

        editarEventoButton.setOnClickListener(v -> editEvent());
    }

    private void showDatePicker() {
        // Configurar el selector de fecha
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Seleccionar Fecha")
                        .build();

        // Manejar la selección de fecha
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Convertir la fecha seleccionada a LocalDate
            LocalDate selectedDate = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.ofEpochDay(selection / (1000 * 60 * 60 * 24));
            }
            // Formatear la fecha y establecerla en el EditText
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                editFechaEvento.setText(selectedDate.format(formatter));
            }
        });

        // Mostrar el selector de fecha
        datePicker.show(getParentFragmentManager(), datePicker.toString());
    }

    private void editEvent() {
        String newEventName = editNombreEvento.getText().toString().trim();
        String newEventDesc = editDescripcionEvento.getText().toString().trim();
        String newEventDateStr = editFechaEvento.getText().toString().trim();

        // Verificar si todos los campos están llenos
        if (newEventName.isEmpty() || newEventDesc.isEmpty() || newEventDateStr.isEmpty()) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si oldEventName sigue siendo el valor obtenido en onCreate o se actualizó
        if (oldEventName == null || oldEventName.isEmpty()) {
            oldEventName = getArguments().getString("oldEventName"); // Obtener oldEventName de los argumentos
        }

        // Parse the date string to LocalDate
        LocalDate newEventDate = null;
        try {
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                newEventDate = LocalDate.parse(newEventDateStr, formatter);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        SessionManager sessionManager = new SessionManager(getContext());
        String username = sessionManager.getUsername();

        if (username != null) {
            PerfilAPI apiService = RetrofitCliente.getInstance().create(PerfilAPI.class);
            Call<Void> call = apiService.editEvent(username, oldEventName, newEventName, newEventDesc, newEventDate);

            if (call != null) {
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Evento editado correctamente", Toast.LENGTH_SHORT).show();
                            NavController navController = Navigation.findNavController(getView());
                            navController.navigate(R.id.navigation_calendar);
                        } else {
                            Toast.makeText(getContext(), "Error al editar el evento: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Error al crear la llamada a la API", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}
