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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.miagenda.Login;
import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Usuario;
import com.example.miagenda.api.retrofit.UsuarioApiCliente;

public class ProfileFragment extends Fragment {

    private TextView nombreUsuarioTextView, emailTextView;
    private SessionManager sessionManager;
    private UsuarioApiCliente usuarioApiClient;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(requireContext());
        usuarioApiClient = new UsuarioApiCliente(); // Inicializa tu cliente de API aquí

        nombreUsuarioTextView = view.findViewById(R.id.nombreUsuarioPerfilTextView);
        emailTextView = view.findViewById(R.id.emailUsuarioTextView);

        Button editarPerfilButton = view.findViewById(R.id.editarPerfilButton);
        editarPerfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_profileFragment_to_editProfileFragment);
            }
        });

        // Obtener el usuario y cargar datos
        String username = sessionManager.getUser().getUsername();
        usuarioApiClient.buscarUsuario(username, new UsuarioApiCliente.UserFetchCallback() {
            @Override
            public void onSuccess(Usuario user) {
                if (user != null) {
                    nombreUsuarioTextView.setText(user.getUsername());
                    emailTextView.setText(user.getEmail());
                } else {
                    // Manejar el caso donde el usuario no es encontrado
                    Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Error al cargar usuario: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // No es necesario cargar de nuevo los datos aquí si se hace en onCreateView()
    }

    Button cerrarSesion;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cerrarSesion = view.findViewById(R.id.cerrarSesionButton);

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
