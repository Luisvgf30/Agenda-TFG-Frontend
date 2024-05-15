package com.example.miagenda.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Usuario;

public class ProfileFragment extends Fragment {

    private TextView nombreUsuarioTextView, emailTextView;
    private SessionManager sessionManager;

    public ProfileFragment() {
        // Required empty public constructor
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
                navController.navigate(R.id.action_profileFragment_to_editProfileFragment);
            }
        });

        loadUserData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData(); // Carga los datos del usuario actualizados
    }

    private void loadUserData() {
        Usuario user = sessionManager.getUser();
        if (user != null) {
            nombreUsuarioTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
        }
    }
}
