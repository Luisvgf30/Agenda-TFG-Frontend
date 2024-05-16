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

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private boolean passwordShowing = false;
    private PerfilAPI perfilAPI;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Obtener referencias a los elementos de la interfaz de usuario
        final EditText usernameET = findViewById(R.id.usernameLogin);
        final EditText passwordET = findViewById(R.id.passwordLogin);
        final ImageView passwordIcon = findViewById(R.id.password_icon);
        final AppCompatButton signIn = findViewById(R.id.signInButtonLogin);
        final AppCompatButton signUp = findViewById(R.id.signUpButtonLogin);

        sessionManager = new SessionManager(this);
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

    private void login(String username, String password) {
        // Crear la llamada para realizar la solicitud de inicio de sesión
        Call<Usuario> call = perfilAPI.logearUsuario(username, password);


        // Enqueue para realizar la llamada asíncrona
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == 200) {
                    // Login exitoso, redirigir a la actividad principal
                    sessionManager.saveUser(response.body());
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();

                } else {
                    // Error en el inicio de sesión
                    if (response.code() == 201) {
                        // Usuario no encontrado o credenciales inválidas
                        Toast.makeText(Login.this, "Credenciales incorrectas. Verifique nuevamente.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Otro error (código de respuesta inesperado)
                        String errorMessage = "Error en el inicio de sesión. Inténtalo de nuevo.";
                        if (response.errorBody() != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.optString("message", null);
                                if (message != null && !message.isEmpty()) {
                                    errorMessage = message;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(Login.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                // Error en la red, mostrar mensaje de error
                Toast.makeText(Login.this, "Error de red. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Error de red: " + t.getMessage(), t);
            }
        });
    }

}
