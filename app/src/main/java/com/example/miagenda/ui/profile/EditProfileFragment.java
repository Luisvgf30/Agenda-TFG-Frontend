package com.example.miagenda.ui.profile;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.UsuarioActualizarRequest;
import com.example.miagenda.api.retrofit.UsuarioApiCliente;

public class EditProfileFragment extends Fragment {

    private UsuarioApiCliente usuarioApiClient;
    private EditText emailEditText, passwordEditText, passwordVerifyEditText;
    private ImageView passwordIcon, confirmarPasswordIcon;
    private SessionManager sessionManager;
    private boolean passwordShowing = false;

    public EditProfileFragment() {
        // Required empty public constructor
    }
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        emailEditText = view.findViewById(R.id.emailusuario);
        passwordEditText = view.findViewById(R.id.passwordUsuario);
        passwordVerifyEditText = view.findViewById(R.id.verifyPasswordUsuario);
        passwordIcon = view.findViewById(R.id.password_icon);
        confirmarPasswordIcon = view.findViewById(R.id.verifypassword_icon);

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
        });

        confirmarPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {
                    passwordShowing = false;
                    passwordVerifyEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmarPasswordIcon.setImageResource(R.drawable.outline_hide_eye_24);
                } else {
                    passwordShowing = true;
                    passwordVerifyEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmarPasswordIcon.setImageResource(R.drawable.outline_show_eye_24);
                    passwordVerifyEditText.setSelection(passwordVerifyEditText.length());
                }
            }
        });

        return view;
    }

    private void updateUserProfile(View v) {
        String newEmail = emailEditText.getText().toString().trim();
        String newPassword = passwordEditText.getText().toString().trim();
        String newPasswordVerify = passwordVerifyEditText.getText().toString().trim();

        if (!newPassword.equals(newPasswordVerify)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = sessionManager.getUser().getUsername();
        UsuarioActualizarRequest updateRequest = new UsuarioActualizarRequest(username, newEmail, newPassword);

        usuarioApiClient.updateUser(username, updateRequest, new UsuarioApiCliente.UserUpdateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                sessionManager.updateUserEmail(newEmail);

                // Navigate back to ProfileFragment
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_editProfileFragment_to_profileFragment);
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
