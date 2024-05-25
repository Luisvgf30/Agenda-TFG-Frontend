package com.example.miagenda.ui.profile;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.UsuarioActualizarRequest;
import com.example.miagenda.api.retrofit.UsuarioApiCliente;

public class EditProfileFragment extends Fragment {

    private UsuarioApiCliente usuarioApiClient;
    private EditText emailEditText, passwordEditText;
    private SessionManager sessionManager;
    private boolean passwordShowing = false;
    private ImageView passwordIcon;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        emailEditText = view.findViewById(R.id.emailusuario);
        passwordEditText = view.findViewById(R.id.passwordUsuario);
        passwordIcon = view.findViewById(R.id.password_icon);

        Button updateButton = view.findViewById(R.id.editarPerfilButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(v);
            }
        });

        sessionManager = new SessionManager(requireContext());
        usuarioApiClient = new UsuarioApiCliente(); // Initialize your API client here

        // Retrieve user data
        Bundle bundle = getArguments();
        if (bundle != null) {
            String email = bundle.getString("email");
            emailEditText.setText(email);
        }

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        return view;
    }

    private void togglePasswordVisibility() {
        if (passwordShowing) {
            passwordShowing = false;
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordIcon.setImageResource(R.drawable.outline_hide_eye_24);
        } else {
            passwordShowing = true;
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordIcon.setImageResource(R.drawable.outline_show_eye_24);
            passwordEditText.setSelection(passwordEditText.length());
        }
    }

    private void updateUserProfile(View v) {
        String newEmail = emailEditText.getText().toString().trim();
        String newPassword = passwordEditText.getText().toString().trim();

        String username = sessionManager.getUser().getUsername();
        UsuarioActualizarRequest updateRequest = new UsuarioActualizarRequest(username, newEmail, newPassword);

        usuarioApiClient.updateUser(username, updateRequest, new UsuarioApiCliente.UserUpdateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                sessionManager.updateUserEmail(newEmail);

                // Navigate back to ProfileFragment
                requireActivity().onBackPressed();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
                Toast.makeText(getContext(), "Error al actualizar perfil: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
