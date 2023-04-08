package com.example.finalpr;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    Intent LoginActiv,RegisterActiv;
    Button login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.loginBtn);
        register = findViewById(R.id.registerBtn);
        LoginActiv = new Intent(this,Login.class);
        RegisterActiv = new Intent(this,Register.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoggedInUser.loggedUser == null)
                    startActivity(LoginActiv);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RegisterActiv);
            }
        });
    }}