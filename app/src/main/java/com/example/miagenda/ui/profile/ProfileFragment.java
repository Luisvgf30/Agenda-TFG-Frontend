// ProfileFragment.java

package com.example.miagenda.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private TextView nombreUsuarioTextView, emailTextView, passwordTextView;
    private SessionManager sessionManager;
    private UsuarioApiCliente usuarioApiClient;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(requireContext());
        usuarioApiClient = new UsuarioApiCliente(); // Inicializa tu cliente de API aquí

        nombreUsuarioTextView = view.findViewById(R.id.nombreUsuarioPerfilTextView);
        emailTextView = view.findViewById(R.id.emailUsuarioTextView);
        passwordTextView = view.findViewById(R.id.passwordUsuarioTextView);

        Button editarPerfilButton = view.findViewById(R.id.editarPerfilButton);
        editarPerfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Bundle para pasar los datos del usuario
                Bundle bundle = new Bundle();
                bundle.putString("username", nombreUsuarioTextView.getText().toString());
                bundle.putString("email", emailTextView.getText().toString());
                bundle.putString("password", passwordTextView.getText().toString());

                // Navegar a la pantalla de edición pasando el Bundle
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_profileFragment_to_editProfileFragment, bundle);
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
                    // Mostrar la contraseña como asteriscos (*****)
                    passwordTextView.setText(getPasswordMask(user.getPassword()));
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

    // Método para enmascarar la contraseña
    private String getPasswordMask(String password) {
        int length = Math.min(password.length(), 5);
        StringBuilder maskedPassword = new StringBuilder();
        for (int i = 0; i < length; i++) {
            maskedPassword.append("*");
        }
        return maskedPassword.toString();
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
