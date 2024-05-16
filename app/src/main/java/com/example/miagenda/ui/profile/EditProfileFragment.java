package com.example.miagenda.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.miagenda.R;
import com.example.miagenda.api.UsuarioActualizarRequest;
import com.example.miagenda.api.retrofit.UsuarioApiCliente;

public class EditProfileFragment extends Fragment {

    private UsuarioApiCliente usuarioApiClient;
    private EditText usernameEditText, emailEditText, passwordEditText;

    public EditProfileFragment() {
        // Constructor vacío requerido por Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        usernameEditText = view.findViewById(R.id.editUsername);
        emailEditText = view.findViewById(R.id.editEmailUser);
        passwordEditText = view.findViewById(R.id.editPasswordUser);

        Button updateButton = view.findViewById(R.id.editarPerfilButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        usuarioApiClient = new UsuarioApiCliente(); // Inicializa el cliente de la API

        return view;
    }

    private void updateUserProfile() {
        String newUsername = usernameEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();
        String newPassword = passwordEditText.getText().toString().trim();

        // Verificar que el nombre de usuario no esté vacío
        if (newUsername.isEmpty()) {
            Toast.makeText(getContext(), "El nombre de usuario no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear la solicitud de actualización del usuario
        UsuarioActualizarRequest updateRequest = new UsuarioActualizarRequest(newUsername, newEmail, newPassword);

        // Llamar al método de actualización del usuario en el cliente de la API
        usuarioApiClient.updateUser(newUsername, updateRequest, new UsuarioApiCliente.UserUpdateCallback() {
            @Override
            public void onSuccess() {
                // Actualización exitosa
                Toast.makeText(getContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();

                // Refrescar los datos en el fragmento de perfil
                if (getActivity() != null) {
                    ProfileFragment profileFragment = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_hostfragment);
                    if (profileFragment != null) {
                        profileFragment.refreshProfileData(); // Actualizar datos del perfil
                    }
                }

                // Volver al fragmento anterior
                requireActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onError(String errorMessage) {
                // Manejar error
                Toast.makeText(getContext(), "Error al actualizar perfil: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton botonAtras = view.findViewById(R.id.boton_atras);

        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vuelve al Fragment anterior
                getParentFragmentManager().popBackStack();
            }

        });
    }
}
