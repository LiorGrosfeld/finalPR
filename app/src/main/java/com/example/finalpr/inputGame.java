package com.example.finalpr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class inputGame extends AppCompatActivity {

    FirebaseHelper fbh;
    EditText name , url,urlImage, desc, linkDisplay; // הטקסט שהוכנס על ידי האדמין, שם, ,תיאור וכתובת
    Button back , insert; // כפתורים חזרה למסך הראשי או הכנסת המשחק למאגר הנתונים
    Intent  out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_site);

        //אתחול משתנים
        name = findViewById(R.id.name2);
        url = findViewById(R.id.url2);
        urlImage = findViewById(R.id.url4);
        back = findViewById(R.id.back);
        insert = findViewById(R.id.Insert);
        desc = findViewById(R.id.description);
        linkDisplay = findViewById(R.id.linkdisplayName);

        // הפניה לרפרנס של ה path למשחקים במאגר נתונים
        fbh =  new FirebaseHelper("Games");

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x= name.getText().toString();
                String y= url.getText().toString();
                String k= urlImage.getText().toString();
                String z= desc.getText().toString();
                if(x.isEmpty() || y.isEmpty() || z.isEmpty())
                {// אם האדמין מנסה להוסיף והטקסט ריק מציג הודעה בהתאם
                    if(x.isEmpty())
                        Toast.makeText(getApplicationContext(),"Game name is empty, input name and try again",Toast.LENGTH_LONG).show();
                    if(y.isEmpty() )
                        Toast.makeText(getApplicationContext(),"Game url is empty, input url and try again",Toast.LENGTH_LONG).show();
                    if(k.isEmpty() )
                        Toast.makeText(getApplicationContext(),"Game url image is empty, input url image and try again",Toast.LENGTH_LONG).show();
                    if(z.isEmpty() )
                        Toast.makeText(getApplicationContext(),"Game description is empty, description and try again",Toast.LENGTH_LONG).show();
                }
                else{// אם לא ריק אז מוסיף
                    try {
                        // מציב את אובייקט המשחק
                        GameObject obj = new GameObject(x,k,z);
                        boolean status = FirebaseHelper.registerGame(obj);
                        if(status) {
                            Toast.makeText(inputGame.this, "Added Successful", Toast.LENGTH_SHORT).show();
                            FirebaseHelper.registerGameSites(y);
                            FirebaseHelper.registerGameSitesName(linkDisplay.getText().toString());
                        }
                        else
                            Toast.makeText(inputGame.this, "Failed to Add", Toast.LENGTH_SHORT).show();
                     name.setText("");// מאפס את הטקסט שהכניס המשתמש כדי שיוכל להקליד שוב
                        url.setText("");
                        urlImage.setText("");
                        desc.setText("");
                        linkDisplay.setText("");
                    }
                    catch (Exception e)
                    {// במקרה של שגיאה כלשהי מציג הודעה בהתאם
                        Toast.makeText(getApplicationContext(),"Error, try again",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        out = new Intent(this,EnterAppPage.class);
        out.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(out);
            }
        });
    }
}