package com.example.finalpr;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class RecoveryActivity extends AppCompatActivity {

    EditText user,ageEdit,mailEdit, recoveredPass;
    Button recover,goback;
    Intent goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        goBack = new Intent(this,MainActivity.class);
        user =  findViewById(R.id.editTextTextUsername);
        ageEdit =  findViewById(R.id.editTextNumberDecimal2);
        mailEdit =  findViewById(R.id.editTextTextMail);
        recover =  findViewById(R.id.buttonRecover);
        goback =  findViewById(R.id.buttongoback);
        recoveredPass =  findViewById(R.id.passwordShowAfterRecovery);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String username = user.getText().toString();
                    String mail = mailEdit.getText().toString();
                    double age = Double.valueOf(ageEdit.getText().toString());

                    String status = FirebaseHelper.isExists(username, age, mail);
                    Snackbar.make(getCurrentFocus(), mail, BaseTransientBottomBar.LENGTH_LONG).show();

                    if (status != null) {
                        recoveredPass.setText(status);
                        recoveredPass.setVisibility(View.VISIBLE);
                        Snackbar.make(getCurrentFocus(), "Found your password!", BaseTransientBottomBar.LENGTH_LONG).show();
                    } else
                        Snackbar.make(getCurrentFocus(), "Didnt found your password!", BaseTransientBottomBar.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(RecoveryActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();

                }
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goBack);
            }
        });
    }

}