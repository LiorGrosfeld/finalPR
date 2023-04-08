package com.example.finalpr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class GameObjectPage extends AppCompatActivity {
    int gameKeyIndex;
    public static List<GameObject> dbGamesList;
    Button back, postCommentbtn;
    Intent out, refresh;
    TextView gameNameText, gameDescriptionText, postComment;
    ListView comments;
    ArrayList<String> all_rating,commentsList,stitesList,stitesNameList;
    ArrayList<Comment>  game_commentsText;
    FirebaseHelper fbh;
    ImageView gameImage;
    Spinner dropdown;
    boolean dropdownBoolisFirstTime = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_object_page);
         fbh  =  new FirebaseHelper("Games");
        // מאתחל משתנים
        game_commentsText = new ArrayList<>();
        commentsList = new ArrayList<>();
        stitesList = new ArrayList<>();
        stitesNameList = new ArrayList<>();
        all_rating = new ArrayList<>();
        postCommentbtn = findViewById(R.id.postCommentbtn);
        gameImage = findViewById(R.id.imageView2);
        postComment = findViewById(R.id.postComment);
        comments = findViewById(R.id.commentsView);
        back = findViewById(R.id.btnGoBack);
        gameNameText = findViewById(R.id.gameName);
        gameDescriptionText = findViewById(R.id.gameDescription);
        out = new Intent(this,ListTable.class);
        out.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        refresh = new Intent(this,GameObjectPage.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         dropdown = findViewById(R.id.spinner1);

        // מקבל ערכים מהעמוד הקודם ומציב אותם
        gameKeyIndex = getIntent().getExtras().getInt("gameKeyIndex");
        gameNameText.setText(getIntent().getExtras().getString("gameName"));
        gameDescriptionText.setText(getIntent().getExtras().getString("gameDescription"));

        new ImageLoadTask(getIntent().getExtras().getString("gameURLimage"), gameImage).execute();

        dbGamesList = FirebaseHelper.convertToGamesList();
        for(int i = 0 ; i< fbh.getSites(gameKeyIndex).size();i++)
            if(fbh.getSites(gameKeyIndex).get(i)!=null)
                stitesList.add(fbh.getSites(gameKeyIndex).get(i));

        for(int i = 0 ; i< fbh.getSitesName(gameKeyIndex).size();i++)
            if(fbh.getSitesName(gameKeyIndex).get(i)!=null)
                stitesNameList.add(fbh.getSitesName(gameKeyIndex).get(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stitesNameList);//stitesList
        dropdown.setAdapter(adapter);

        for(int i = 0 ; i< fbh.getComments(gameKeyIndex).size();i++)
            if(fbh.getComments(gameKeyIndex).get(i)!=null)
        commentsList.add(fbh.getComments(gameKeyIndex).get(i));
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,commentsList);
        comments.setAdapter(arrayAdapter);

        comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        comments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.e("URLDISPLAY", stitesList.get(position));
                String siteURL = stitesList.get(position);
                if(dropdownBoolisFirstTime) {
                    dropdownBoolisFirstTime = false;
                }
                else {
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(stitesList.get(position)));
                    startActivity(browse);
                    browse.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        postCommentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try {
                      String x = postComment.getText().toString();
                        // מציב את אובייקט המשחק
                        boolean status = FirebaseHelper.registerComment(new Comment(x,LoggedInUser.loggedUser.getUserName()),gameKeyIndex);
                        if(status)
                            Toast.makeText(GameObjectPage.this, "Added Successful", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(GameObjectPage.this, "Failed to Add", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {// במקרה של שגיאה כלשהי מציג הודעה בהתאם
                        Toast.makeText(getApplicationContext(),"Error, check game  URL, if not vaild edit to a valid one and try again",Toast.LENGTH_SHORT).show();
                    }
                    // מרפרש את הדף, לוקח את הנתונים שקיבל ומציב כדי לפתוח את הדף מחדש עם התגובה המעודכת
                refresh.putExtra("gameKeyIndex",gameKeyIndex);
                refresh.putExtra("gameName",getIntent().getExtras().getString("gameName"));
                refresh.putExtra("gameDescription",getIntent().getExtras().getString("gameDescription"));
                refresh.putExtra("gameURLimage",getIntent().getExtras().getString("gameURLimage"));
                startActivity(refresh);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(out);
        }
    });
    }
}