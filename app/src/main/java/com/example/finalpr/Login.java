package com.example.finalpr;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button login, back;
    EditText username,password;
    TextView recoverPass;
    Intent GoIn,GoOut;
    FirebaseHelper fbh;
    Thread thread;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fbh =  new FirebaseHelper("Users"); // מכין את הדרך למשתמשים בממסד הנתונים
        username = findViewById(R.id.nameInput);
        password = findViewById(R.id.passInput);
        login = findViewById(R.id.loginActivityBtn);
        back = findViewById(R.id.loginActivityBtnBack);
        recoverPass = findViewById(R.id.textView10);
        GoIn = new Intent(this,EnterAppPage.class); // מכין את דף הכניסה
        GoOut = new Intent(this,MainActivity.class); // מכין את דף היציאה

        recoverPass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),RecoveryActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // לפני הכניסה מריץ מספר בדיקות
                if(username.getText().toString().length() < 3) {
                    Toast.makeText(Login.this, "Username is too short", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length() < 1) {
                    Toast.makeText(Login.this, "Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                thread = new Thread() {
                    @Override
                    public void run() { //  מכין את המשתמש המחובר כעת
                        super.run();
                        LoggedInUser.prepare();
                        LoggedInUser.prepareSt();
                        boolean status = FirebaseHelper.login(username.getText().toString(), password.getText().toString());
                        if (status) {
                            Toast.makeText(Login.this, "Login Sucssesful", Toast.LENGTH_SHORT).show();
                            startActivity(GoIn);
                        }
                        else
                            Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }};
                handler =  new Handler();
                handler.postDelayed(thread,1000);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GoOut);
            }
        });
    }
}