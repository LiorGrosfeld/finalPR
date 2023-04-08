package com.example.finalpr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class Register extends AppCompatActivity {
    Button register, back;
    EditText username,password,passwordAgain,mail, age;
    Intent GoOut;
    FirebaseHelper fbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.registerUsernameInput);
        password = findViewById(R.id.registerPassInput);
        passwordAgain = findViewById(R.id.registerPassAgainInput);
        mail = findViewById(R.id.registerMailInput);
        register = findViewById(R.id.registerGoRegister);
        back = findViewById(R.id.registerBack);
        age = findViewById(R.id.editTextNumberDecimal);
        GoOut = new Intent(this,MainActivity.class);
        fbh =  new FirebaseHelper("Users");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValidEmail(mail.getText()); // בודק אם המייל תקין
                if(!password.getText().toString().equals(passwordAgain.getText().toString())) // בודק אם הסיסמא זהה לסיסמא בשנית
                {
                    Toast.makeText(getBaseContext(),"Passwords dont match!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(username.getText().length() < 3) // בודק שהשם משתמש מכיל לפחות 3 אותיות
                        Toast.makeText(getBaseContext(),"Username is too short(3+ chars)",Toast.LENGTH_SHORT).show();
                    else if(!mail.getText().toString().contains("@") && !mail.getText().toString().contains("."))
                        Toast.makeText(getBaseContext(),"The mail is not in the correct format",Toast.LENGTH_SHORT).show();
                    else
                    {
                        // רושם משתמש זמני בשביל בדיקות
                        User temp = new User(username.getText().toString(),password.getText().toString(),Double.valueOf(age.getText().toString()),mail.getText().toString());
                        if(FirebaseHelper.isExists(username.getText().toString())) // אם השם משתמש כבר קיים בממסד נתונים
                        {
                            Toast.makeText(Register.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean status = FirebaseHelper.register(temp); // רושם את המשתמש ומחלקה של ממסד הנתונים ומציג הודעה בהתאם

                        if(status)
                            Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GoOut);
            }
        });
    }
    public void isValidEmail(CharSequence target) {
        if (target == null)
            Toast.makeText(Register.this, "Mail is empty", Toast.LENGTH_SHORT).show();
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches())
                Toast.makeText(Register.this, "Mail is invalid", Toast.LENGTH_SHORT).show();
        }
}