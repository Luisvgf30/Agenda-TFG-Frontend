package com.example.miagenda.ui.notes;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Nota;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesFragment extends Fragment {

    private SessionManager sessionManager;
    private List<Nota> notas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayout noNotesContainer;
    private AdapterNotes adapter;

    public NotesFragment() {
        // Constructor público requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño para este fragmento
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar sessionManager
        sessionManager = new SessionManager(requireContext());

        String username = sessionManager.getUsername();

        if (username == null) {
            Toast.makeText(requireContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        TextInputEditText editText = view.findViewById(R.id.inputNotes);
        ImageButton imageButton = view.findViewById(R.id.buttonNotes);
        recyclerView = view.findViewById(R.id.recycler_view);
        noNotesContainer = view.findViewById(R.id.no_notes_container);

        adapter = new AdapterNotes(notas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        updateNoNotesView();

        imageButton.setOnClickListener(v -> {
            String message = editText.getText().toString().trim();
            if (!message.isEmpty()) {
                // Llamar a la API para crear una nota
                PerfilAPI notesAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);
                Call<Void> call = notesAPI.createNote(message, username);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // La nota se creó correctamente, ahora cargar las notas del usuario
                            loadUserNotes(username);
                            editText.setText(""); // Limpiar el campo de texto después de agregar la nota
                        } else {
                            // Manejar el fallo en la creación de la nota
                            Toast.makeText(requireContext(), "Error al crear la nota: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Manejar fallos de red
                        Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Cargar las notas del usuario inicialmente
        loadUserNotes(username);
    }

    private void loadUserNotes(String username) {
        PerfilAPI notesAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);
        Call<List<Nota>> call = notesAPI.buscarNotes(username);

        call.enqueue(new Callback<List<Nota>>() {
            @Override
            public void onResponse(Call<List<Nota>> call, Response<List<Nota>> response) {
                if (response.isSuccessful()) {
                    List<Nota> notasResponse = response.body();
                    notas.clear();
                    if (notasResponse != null) {
                        notas.addAll(notasResponse);
                    }
                    adapter.notifyDataSetChanged();
                    updateNoNotesView();
                } else {
                    // Manejar el fallo al cargar las notas del usuario
                    Toast.makeText(requireContext(), "Error al cargar las notas: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Nota>> call, Throwable t) {
                // Manejar fallos de red
                Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNoNotesView() {
        if (notas.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noNotesContainer.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noNotesContainer.setVisibility(View.GONE);
        }
    }
}
