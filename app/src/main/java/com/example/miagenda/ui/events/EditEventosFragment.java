package com.example.miagenda.ui.events;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEventosFragment extends Fragment {

    private EditText editNombreEvento;
    private EditText editDescripcionEvento;
    private EditText editFechaEvento;
    private EditText editAvisoEvento;
    private Button editarEventoButton;

    private String oldEventName;

    public EditEventosFragment() {
        // Required empty public constructor
    }

    public static EditEventosFragment newInstance(String oldEventName, String eventDesc, String eventDate, String avisoEvento) {
        EditEventosFragment fragment = new EditEventosFragment();
        Bundle args = new Bundle();
        args.putString("oldEventName", oldEventName);
        args.putString("eventDesc", eventDesc);
        args.putString("eventDate", eventDate);
        args.putString("avisoEvento", avisoEvento);
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
        editAvisoEvento = view.findViewById(R.id.editAvisoEvento);
        editarEventoButton = view.findViewById(R.id.editarEventoButton);

        ImageButton botonAtras = view.findViewById(R.id.boton_atras);
        botonAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        if (getArguments() != null) {
            oldEventName = getArguments().getString("oldEventName");
            editNombreEvento.setText(oldEventName);
            editDescripcionEvento.setText(getArguments().getString("eventDesc"));
            editFechaEvento.setText(getArguments().getString("eventDate"));
            editAvisoEvento.setText(getArguments().getString("avisoEvento"));
        }

        editarEventoButton.setOnClickListener(v -> editEvent());
    }

    private void editEvent() {
        String newEventName = editNombreEvento.getText().toString();
        String newEventDesc = editDescripcionEvento.getText().toString();
        String newEventDateStr = editFechaEvento.getText().toString();
        String avisoEvento = editAvisoEvento.getText().toString();

        // Verificar si oldEventName sigue siendo el valor obtenido en onCreate o se actualizó
        if (oldEventName == null || oldEventName.isEmpty()) {
            oldEventName = getArguments().getString("oldEventName"); // Obtener oldEventName de los argumentos
        }

        // Parse the date string to LocalDate
        LocalDate newEventDate = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            newEventDate = LocalDate.parse(newEventDateStr, formatter);
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
                            Toast.makeText(getContext(), "Error al editar el evento", Toast.LENGTH_SHORT).show();
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
