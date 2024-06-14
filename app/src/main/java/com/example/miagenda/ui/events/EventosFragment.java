package com.example.miagenda.ui.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Evento;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosFragment extends Fragment {

    private List<Evento> events = new ArrayList<>();
    private LinearLayout noEventsContainer;
    private EventAdapter adapter;
    private RecyclerView recyclerView;
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
        recyclerView = view.findViewById(R.id.recyclerViewEventos);
        noEventsContainer = view.findViewById(R.id.no_events_container);

        FloatingActionButton fabAgregarEvento = view.findViewById(R.id.fabAgregarEvento);
        fabAgregarEvento.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.addCalendarFragment);
        });

        adapter = new EventAdapter(events, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

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
                        events.clear();
                        events.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        updateNoEventsView();
                    }
                }
                @Override
                public void onFailure(Call<List<Evento>> call, Throwable t) {
                    Log.d("LoadEvents", "Error al cargar eventos: " + t.getMessage());
                }
            });
        }
    }

    public void deleteEvent(String eventName, int position) {
        String username = sessionManager.getUsername();
        if (username != null) {
            RetrofitCliente.getInstance().create(PerfilAPI.class).deleteEvent(username, eventName).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("DeleteEvent", "Evento eliminado correctamente.");
                        events.remove(position);
                        adapter.notifyItemRemoved(position);
                        updateNoEventsView();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("DeleteEvent", "Error al eliminar el evento: " + t.getMessage());
                }
            });
        }
    }

    private void updateNoEventsView() {
        if (events.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noEventsContainer.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noEventsContainer.setVisibility(View.GONE);
        }
    }
}
