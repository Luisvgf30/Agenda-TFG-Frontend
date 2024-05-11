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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miagenda.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NotesFragment extends Fragment {

    private List<String> messages = new ArrayList<>();
    private RecyclerView recyclerView;

    private LinearLayout noNotesContainer;
    private MyAdapter adapter;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText editText = view.findViewById(R.id.inputNotes);
        ImageButton imageButton = view.findViewById(R.id.buttonNotes);
        recyclerView = view.findViewById(R.id.recycler_view);
        noNotesContainer = view.findViewById(R.id.no_notes_container);

        adapter = new MyAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        updateNoNotesView();

        imageButton.setOnClickListener(v -> {
            String message = editText.getText().toString();
            if (!message.trim().isEmpty()) {
                messages.add(message);
                adapter.notifyItemInserted(messages.size() - 1);
                recyclerView.smoothScrollToPosition(messages.size() - 1);
                editText.setText(""); // Limpiar el TextInputEditText después de agregar la nota
                updateNoNotesView();
            }
        });
    }

    private void updateNoNotesView() {
        if (messages.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noNotesContainer.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noNotesContainer.setVisibility(View.GONE);
        }
    }


}