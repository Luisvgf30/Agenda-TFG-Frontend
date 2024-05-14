package com.example.miagenda.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.miagenda.Login;
import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Usuario;

public class ProfileFragment extends Fragment {

    private TextView nombreUsuarioTextView;
    private TextView emailTextView;
    private SessionManager sessionManager;

    public ProfileFragment() {
        // Constructor vacío requerido por Fragment
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData(); // Cargar datos al reanudar el fragmento
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(requireContext());

        nombreUsuarioTextView = view.findViewById(R.id.nombreUsuarioDesc);
        emailTextView = view.findViewById(R.id.emailDesc);

        Button editarPerfilButton = view.findViewById(R.id.editarPerfil);
        editarPerfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.editarPerfil);
            }
        });

        loadUserData();

        return view;
    }

    private void loadUserData() {
        Usuario user = sessionManager.getUser();
        if (user != null) {
            // Mostrar datos del usuario en las vistas
            nombreUsuarioTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
        }
    }

    public void refreshProfileData() {
        loadUserData();
    }

    Button cerrarSesion;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cerrarSesion = view.findViewById(R.id.cerrarSesion);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Cerrar sesión")
                        .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), Login.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

}
