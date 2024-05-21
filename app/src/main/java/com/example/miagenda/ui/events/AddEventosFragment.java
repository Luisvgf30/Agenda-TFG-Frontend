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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventosFragment extends Fragment {

    private EditText addNombreEvento;
    private EditText addDescripcionEvento;
    private EditText addFechaEvento;
    private EditText addAvisoEvento;
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
        addAvisoEvento = view.findViewById(R.id.addAvisoEvento);
        addEventoButton = view.findViewById(R.id.addEventoButton);

        ImageButton botonAtras = view.findViewById(R.id.boton_atras);
        botonAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        addEventoButton.setOnClickListener(v -> createEvent());
    }

    private void createEvent() {
        String eventName = addNombreEvento.getText().toString();
        String eventDesc = addDescripcionEvento.getText().toString();
        LocalDate eventDate = null; // Assuming eventDate is a String. Convert as needed.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            eventDate = LocalDate.parse(addFechaEvento.getText().toString());
        }
        String avisoEvento = addAvisoEvento.getText().toString(); // Same assumption as eventDate.

        // Obtener el usuario de la sesión
        SessionManager sessionManager = new SessionManager(getContext());
        String username = sessionManager.getUsername(); // Obtener el nombre de usuario desde la sesión

        if (username != null) {
            // Crear la solicitud para añadir el evento
            PerfilAPI apiService = RetrofitCliente.getInstance().create(PerfilAPI.class);
            Call<Void> call = apiService.createEvent(username, eventName, eventDesc, eventDate);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Evento añadido correctamente", Toast.LENGTH_SHORT).show();
                        // Navegar de vuelta a EventosFragment
                        NavController navController = Navigation.findNavController(getView());
                        navController.navigate(R.id.navigation_calendar);
                    } else {
                        Toast.makeText(getContext(), "Error al añadir el evento", Toast.LENGTH_SHORT).show();
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
