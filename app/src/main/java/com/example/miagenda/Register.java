package com.example.miagenda;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
        private TextView signInBtn;
        private boolean passwordShowing = false;

        private PerfilAPI perfilAPI;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            // Inicialización de vistas
            emailET = findViewById(R.id.emailSignUp);
            usuarioET = findViewById(R.id.usernameSingUp);
            passwordET = findViewById(R.id.passwordSignUp);
            confirmarPasswordET = findViewById(R.id.confirmPasswordSignUp);
            passwordIcon = findViewById(R.id.password_icon1);
            confirmarPasswordIcon = findViewById(R.id.password_icon2);
            signInBtn = findViewById(R.id.signInRegister);


            // Inicialización de Retrofit y PerfilAPI
            perfilAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);

            // Configuración del botón de registro
            findViewById(R.id.signUpButtonRegister).setOnClickListener(new View.OnClickListener() {
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

            confirmarPasswordIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (passwordShowing) {
                        passwordShowing = false;
                        confirmarPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        confirmarPasswordIcon.setImageResource(R.drawable.outline_hide_eye_24);
                    } else {
                        passwordShowing = true;
                        confirmarPasswordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        confirmarPasswordIcon.setImageResource(R.drawable.outline_show_eye_24);
                        confirmarPasswordET.setSelection(confirmarPasswordET.length());
                    }
                }
            });

            signInBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    finish();
                }
            });
        }

        private void registrarUsuario(String email, String username, String password) {
            Usuario usuario = new Usuario(email, username, password);
            Call<Usuario> call = perfilAPI.registrarUsuario(email, username, password);

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.isSuccessful()) {
                            // Registro exitoso
                            Usuario usuario = response.body();

                            // Redirigir a la pantalla de inicio de sesión (LoginActivity)
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                            finish(); // Esto termina la actividad actual (Register) para que no pueda volver atrás con el botón "Atrás"
                        } else {
                            // Manejar errores de registro
                            if (response.code() == 400) {
                                // Error de datos duplicados (nombre de usuario o email existen)
                                Toast.makeText(Register.this, "Nombre de usuario o email ya existe", Toast.LENGTH_SHORT).show();
                            } else {
                                // Otro tipo de error
                                Toast.makeText(Register.this, "Error en el registro. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                            }
                        }
                }
                    @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    // Manejar errores de red
                    Toast.makeText(Register.this, "Error de red. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
