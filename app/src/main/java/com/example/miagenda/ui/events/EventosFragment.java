package com.example.miagenda.ui.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Evento;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosFragment extends Fragment {

    private LinearLayout containerEventos;
    private SessionManager sessionManager;

    public EventosFragment() {
        // Required empty public constructor
    }

    public static EventosFragment newInstance(String param1, String param2) {
        EventosFragment fragment = new EventosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);
        containerEventos = view.findViewById(R.id.containerEventos);

        FloatingActionButton fabAgregarEvento = view.findViewById(R.id.fabAgregarEvento);
        fabAgregarEvento.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.addCalendarFragment);
        });

        loadEvents();

        return view;
    }

    private void loadEvents() {
        String username = sessionManager.getUsername();
        if (username != null) {
            RetrofitCliente.getInstance().create(PerfilAPI.class).buscarEvents(username).enqueue(new Callback<List<Evento>>() {
                @Override
                public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        displayEvents(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Evento>> call, Throwable t) {
                    // Handle failure
                }
            });
        }
    }

    private void displayEvents(List<Evento> eventos) {
        for (Evento evento : eventos) {
            Log.d("Evento", "Nombre: " + evento.getName_event() + ", Fecha: " + evento.getEvent_date());

            View eventView = getLayoutInflater().inflate(R.layout.event_card, containerEventos, false);

            TextView nombreEvento = eventView.findViewById(R.id.nombreEvento);
            TextView fechaYhoraEvento = eventView.findViewById(R.id.fechaYhoraEvento);
            CardView cardView = eventView.findViewById(R.id.eventoCV);
            Button deleteButton = eventView.findViewById(R.id.deleteEvento);

            nombreEvento.setText(evento.getName_event());
            fechaYhoraEvento.setText(evento.getEvent_date());  // Formatear la fecha si es necesario

            deleteButton.setOnClickListener(v -> {
                deleteEvent(evento.getName_event()); // Llama al método para eliminar el evento
                containerEventos.removeView(eventView); // Elimina la vista de la carta del contenedor
            });

            cardView.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(v);
                // Crear un Bundle para pasar los datos del evento seleccionado a MyEventFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("evento", evento);


                // Navegar a MyEventFragment y pasar el Bundle como argumento
                navController.navigate(R.id.myEvent, bundle);
            });

            containerEventos.addView(eventView);
        }
    }

    private void deleteEvent(String eventName) {
        String username = sessionManager.getUsername();
        if (username != null) {
            RetrofitCliente.getInstance().create(PerfilAPI.class).deleteEvent(username, eventName).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // La eliminación fue exitosa
                        Log.d("DeleteEvent", "Evento eliminado correctamente");
                    } else {
                        // La eliminación falló
                        Log.e("DeleteEvent", "Error al eliminar el evento");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Handle failure
                    Log.e("DeleteEvent", "Error de red: " + t.getMessage());
                }
            });
        }
    }
}
