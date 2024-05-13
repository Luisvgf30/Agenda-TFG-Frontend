package com.example.miagenda;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    private boolean passwordShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        final EditText email = findViewById(R.id.emailET);
        final EditText username = findViewById(R.id.usernameET);
        final EditText password = findViewById(R.id.passwordET);
        final EditText conPassword = findViewById(R.id.confirmPasswordET);
        final ImageView passwordIcon = findViewById(R.id.password_icon1);
        final ImageView conPasswordIcon = findViewById(R.id.password_icon2);
        final AppCompatButton signUpBtn = findViewById(R.id.signUp);
        final TextView signInBtn = findViewById(R.id.signIn);


        passwordIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(passwordShowing){
                    passwordShowing = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.outline_hide_eye_24);
                }else{
                    passwordShowing = true;

                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.outline_show_eye_24);

                    password.setSelection(password.length());
                }
            }
        });

        conPasswordIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(passwordShowing){
                    passwordShowing = false;
                    conPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.outline_hide_eye_24);
                }else{
                    passwordShowing = true;

                    conPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.outline_show_eye_24);

                    conPassword.setSelection(conPassword.length());
                }
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Register.this, MainActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }




}