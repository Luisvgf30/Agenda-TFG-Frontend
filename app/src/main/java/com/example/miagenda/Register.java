package com.example.miagenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miagenda.api.Usuario;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private EditText emailET, usuarioET, passwordET, confirmarPasswordET;
    private ImageView passwordIcon, confirmarPasswordIcon;
    private boolean passwordShowing = false;

    private PerfilAPI perfilAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicialización de vistas
        emailET = findViewById(R.id.estadoET);
        usuarioET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        confirmarPasswordET = findViewById(R.id.confirmPasswordET);
        passwordIcon = findViewById(R.id.password_icon1);
        confirmarPasswordIcon = findViewById(R.id.password_icon2);

        // Inicialización de Retrofit y PerfilAPI
        perfilAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);

        // Configuración del botón de registro
        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                String usuario = usuarioET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String confirmarPassword = confirmarPasswordET.getText().toString().trim();

                if (!email.isEmpty() && !usuario.isEmpty() && !password.isEmpty() && !confirmarPassword.isEmpty()) {
                    if (password.equals(confirmarPassword)) {
                        registrarUsuario(email, usuario, password);
                    } else {
                        Toast.makeText(Register.this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registrarUsuario(String email, String username, String password) {
        Usuario usuario = new Usuario(email, username, password); // Crear un objeto Usuario
        Call<Void> call = perfilAPI.registrarUsuario(usuario); // Llamar al método de registro con el objeto Usuario

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Registro exitoso, navegar a la actividad principal o mostrar mensaje
                    startActivity(new Intent(Register.this, MainActivity.class));
                    finish();
                } else {
                    // Manejar errores de registro (por ejemplo, usuario ya registrado)
                    Toast.makeText(Register.this, "Error en el registro. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Manejar errores de red
                Toast.makeText(Register.this, "Error de red. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
