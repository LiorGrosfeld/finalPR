package com.example.finalpr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class EnterAppPage extends AppCompatActivity {
    Button Input , ToList,deleteStoredData,exist, logOut;//  כפתורי מעבר חלונות
    Intent in , inputNewGame , out2,refresh; // אינטנטים למעבר לחלונות
    ArrayList<String>  game_description,site_comments; // רשימת מערכים של הנתונים
    TextView gamesCount; //  כמות המשחקים
    FirebaseHelper fbh; // הפניה למאגר נתונים
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_app_page);
        // אתחול משתנים
        fbh =  new FirebaseHelper("Games");
        Input = findViewById(R.id.input);
        exist = findViewById(R.id.existApp);
        ToList = findViewById(R.id.ToList);
        deleteStoredData = findViewById(R.id.deleteall);
        gamesCount = findViewById(R.id.textView2);
        logOut = findViewById(R.id.logoutbtn);
        inputNewGame = new Intent(this, inputGame.class);
        out2 = new Intent(this,ListTable.class);
        in = getIntent();
        refresh = getIntent();
        refresh = new Intent(this,EnterAppPage.class);
        game_description = new ArrayList<>();
        site_comments = new ArrayList<>();
        Log.d("FB", "Games Size: "+ fbh.fromDBListGames.size());
        gamesCount.setText("Currently "+fbh.fromDBListGames.size()+" games stored in the Hubb");

        // רק אם המשתמש הוא אדמין הוא יוכל להוסיף משחקים
                 // או למחוק את כל המשחקים מתוך האפליקציה
            if(LoggedInUser.loggedUser.getAccessLevel() != 1) {
                Input.setVisibility(View.INVISIBLE);
                deleteStoredData.setVisibility(View.INVISIBLE);
            }
            // דיאלוג יציאה מהמשתמש התחלה
        DialogInterface.OnClickListener dialogListner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        LoggedInUser.loggedUser=null;
                        Toast.makeText(EnterAppPage.this, "Loggedout Sucssesful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Logout?").setPositiveButton("Yes",dialogListner).setNegativeButton("No",dialogListner);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        // דיאלוג יציאה מהמשתמש סוף

        // מחיקה של כל הערכים ברשימת המשחקים
        deleteStoredData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!FirebaseHelper.convertToGamesList().isEmpty())
                    confirmDialog();
                else
                    Toast.makeText(getApplicationContext(), "list is empty",Toast.LENGTH_SHORT).show();
            }
        });
        Input.setOnClickListener(new View.OnClickListener() {
            @Override // עובר לחלון של הוספת משחק חדש למאגר הנתונים
            public void onClick(View v) {
                startActivity(inputNewGame);
            }
        });
        exist.setOnClickListener(new View.OnClickListener() {
            @Override // סוגר את האפליקציה
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        ToList.setOnClickListener(new View.OnClickListener() {
            @Override // עובר לחלון של הצגת המשחקים ממאגר הנתונים
            public void onClick(View v) {
                startActivity(out2);
            }
        });
    }
    // דיאלוג מחיקה של כל המשחקים
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all Games data");
        builder.setMessage("Are you sure you want to delete the entire Games list?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseHelper.deleteGamesList();
                startActivity(refresh);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
