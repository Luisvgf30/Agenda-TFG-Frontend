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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventosFragment extends Fragment {

    private EditText addNombreEvento;
    private EditText addDescripcionEvento;
    private EditText addFechaEvento;
    private Button addEventoButton;

    public AddEventosFragment() {
        // Required empty public constructor
    }

    public static AddEventosFragment newInstance(String param1, String param2) {
        AddEventosFragment fragment = new AddEventosFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_eventos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addNombreEvento = view.findViewById(R.id.addNombreEvento);
        addDescripcionEvento = view.findViewById(R.id.addDescripcionEvento);
        addFechaEvento = view.findViewById(R.id.addFechaEvento);
        addEventoButton = view.findViewById(R.id.addEventoButton);

        ImageButton botonAtras = view.findViewById(R.id.boton_atras);
        botonAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        addFechaEvento.setOnClickListener(v -> showDatePicker());

        addEventoButton.setOnClickListener(v -> createEvent());
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Selecciona una fecha");
        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            LocalDate selectedDate = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.ofEpochDay(selection / (24 * 60 * 60 * 1000));
            }
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                addFechaEvento.setText(selectedDate.format(formatter));
            }
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void createEvent() {
        String eventName = addNombreEvento.getText().toString().trim();
        String eventDesc = addDescripcionEvento.getText().toString().trim();
        String eventDateStr = addFechaEvento.getText().toString().trim();

        // Verificar si todos los campos están llenos
        if (eventName.isEmpty() || eventDesc.isEmpty() || eventDateStr.isEmpty()) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate eventDate = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                eventDate = LocalDate.parse(eventDateStr, formatter);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        SessionManager sessionManager = new SessionManager(getContext());
        String username = sessionManager.getUsername();

        if (username != null) {
            PerfilAPI apiService = RetrofitCliente.getInstance().create(PerfilAPI.class);
            Call<Void> call = apiService.createEvent(username, eventName, eventDesc, eventDate);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Evento añadido correctamente", Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(getView());
                        navController.navigate(R.id.navigation_calendar);
                    } else {
                        Toast.makeText(getContext(), "Error al añadir el evento: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}
