package com.example.miagenda.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Usuario;
import com.google.gson.Gson;

public class ProfileFragment extends Fragment {

    private Usuario usuario;
    private SessionManager sessionManager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        usuario = sessionManager.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (usuario != null) {
            TextView nombreUsuarioDesc = view.findViewById(R.id.nombreUsuarioDesc);
            TextView emailDesc = view.findViewById(R.id.emailDesc);
            TextView passwordDesc = view.findViewById(R.id.passwordDesc);

            nombreUsuarioDesc.setText(usuario.getUsername());
            emailDesc.setText(usuario.getEmail());
            passwordDesc.setText("*******"); // Puedes establecer la contraseña como desees
        }

        return view;
    }
}

