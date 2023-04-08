package com.example.finalpr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AdminPageEdit extends AppCompatActivity {
    TextView editName,editDescription,addWebsite,editImageUrl, WebsiteDisplayName;
    Button saveNameBtn,saveDescriptionBtn,applyAddWebsiteBtn,editImageUrlBtn, goBackBtn;
    FirebaseHelper fbh;
    int gameKeyIndex;
    Intent GoOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_edit);
            // מאתחל משתנים
        WebsiteDisplayName = findViewById(R.id.addWebsiteDisplayName);
        editName = findViewById(R.id.editGameName);
        goBackBtn = findViewById(R.id.goBackBtn);
        editDescription = findViewById(R.id.editDescription);
        addWebsite = findViewById(R.id.AddWebsite);
        editImageUrl = findViewById(R.id.editImageURL);
        saveNameBtn = findViewById(R.id.applyNameChange);
        saveDescriptionBtn = findViewById(R.id.applyDescriptionChange);
        applyAddWebsiteBtn = findViewById(R.id.applyAddWebsite);
        editImageUrlBtn = findViewById(R.id.applyEditImageURL);
        //מביא את הpath שבו נמצאים המשחקים מהבסיס נתונים
        fbh  =  new FirebaseHelper("Games");
        GoOut = new Intent(this,ListTable.class);
        //מקבל מהעמוד הקודם את האינדקס של המשחק שנבחר
        gameKeyIndex = getIntent().getExtras().getInt("gameKeyIndex");
        //משנה שם
        saveNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editName.getText().toString();
                try {
                        fbh.editGameName(gameKeyIndex,newName);
                    Toast.makeText(getApplicationContext(), "Saved Successfully",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {// במקרה של שגיאה כלשהי מציג הודעה בהתאם
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //משנה תיאור
        saveDescriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDescription = editDescription.getText().toString();
                try {
                        fbh.editGameDescription(gameKeyIndex,newDescription);
                    Toast.makeText(getApplicationContext(), "Saved Successfully",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {// במקרה של שגיאה כלשהי מציג הודעה בהתאם
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //חוזר אחורה
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(GoOut);
            }
        });
        //מוסיף אתר לרשימת האתרים של המשחק
        applyAddWebsiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newURL = addWebsite.getText().toString();
                String newURLName = WebsiteDisplayName.getText().toString();
                if(!newURL.isEmpty() && !newURLName.isEmpty())
                try {
                    //פותח את האתר שהוכנס לראות שהוא זמין, אם לא אז אפשר למוק/לשנות
                    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( newURL ) );
                    startActivity(browse);
                    browse.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //אם אתר תקין מוסיף אותו
                    fbh.addGameWebsite(gameKeyIndex,newURL);
                    fbh.addGameWebsiteName(gameKeyIndex,newURLName);
                    Toast.makeText(getApplicationContext(), "Saved Successfully",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {// במקרה של שגיאה כלשהי מציג הודעה בהתאם
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //משנה את תמונת המשחק שנבחר
        editImageUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newImageURL = editImageUrl.getText().toString();
                try {
                    //פותח את האתר שהוכנס לראות שהוא זמין, אם לא אז אפשר למוק/לשנות
                    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( newImageURL ) );
                    startActivity(browse);
                    browse.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //אם אתר תקין מוסיף אותו
                    fbh.editGameImageURL(gameKeyIndex,newImageURL);
                    Toast.makeText(getApplicationContext(), "Saved Successfully",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {// במקרה של שגיאה כלשהי מציג הודעה בהתאם
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}