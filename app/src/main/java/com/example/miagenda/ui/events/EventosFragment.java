package com.example.miagenda.ui.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
                        events.clear();
                        events.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        updateNoEventsView();
                    }
                }

                @Override
                public void onFailure(Call<List<Evento>> call, Throwable t) {
                    // Handle failure
                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewEventos);
        noEventsContainer = view.findViewById(R.id.no_events_container);
        containerEventos = view.findViewById(R.id.containerEventos);

        adapter = new EventAdapter(events);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        updateNoEventsView();
    }

    private void updateNoEventsView() {
        if (events.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noEventsContainer.setVisibility(View.VISIBLE);
            containerEventos.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noEventsContainer.setVisibility(View.GONE);
            containerEventos.setVisibility(View.VISIBLE);
        }
    }
}
