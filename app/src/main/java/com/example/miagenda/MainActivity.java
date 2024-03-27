package com.example.miagenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button signinButton = findViewById(R.id.signin_login);
        Button signupButton = findViewById(R.id.signup_login);

        signinButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.signin_login){
            intent = new Intent(this, Profile.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.signup_login){
            intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }

    }
}