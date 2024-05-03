package com.example.miagenda;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.miagenda.api.Usuario;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private boolean passwordShowing = false;
    private PerfilAPI perfilAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Obtener referencias a los elementos de la interfaz de usuario
        final EditText usernameET = findViewById(R.id.usernameET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final ImageView passwordIcon = findViewById(R.id.password_icon);
        final AppCompatButton signIn = findViewById(R.id.signIn);
        final AppCompatButton signUp = findViewById(R.id.signUp);

        // Inicializar la instancia de PerfilAPI utilizando RetrofitCliente
        perfilAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);

        // Configurar el click listener para mostrar/ocultar la contraseña
        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.outline_hide_eye_24);
                } else {
                    passwordShowing = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.outline_show_eye_24);
                    passwordET.setSelection(passwordET.length());
                }
            }
        });

        // Configurar el click listener para el botón de "Sign In"
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                // Verificar las credenciales del usuario al hacer login
                login(username, password);
            }
        });

        // Configurar el click listener para el botón de "Sign Up"
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    // Método para realizar la verificación del usuario utilizando Retrofit
    // Método para realizar la verificación del usuario utilizando Retrofit
    private void login(String username, String password) {
        Call<Usuario> call = perfilAPI.logearUsuario(username, password);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Login exitoso, abrir la actividad principal
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                } else {
                    // Error en las credenciales, mostrar mensaje de error
                    Toast.makeText(Login.this, "Error en el inicio de sesión. Verifique las credenciales.", Toast.LENGTH_SHORT).show();

                    // Imprimir el error en el logcat
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e("LoginActivity", "Error en el inicio de sesión: " + errorResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("LoginActivity", "Error en el inicio de sesión: respuesta sin cuerpo");
                    }
                }
            }


            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                // Mostrar mensaje de error en Toast
                Toast.makeText(Login.this, "Error en la red. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();

                // Imprimir el mensaje de error en el Logcat
                Log.e("Retrofit", "Error de red: " + t.getMessage(), t);
            }
        });
    }

}
