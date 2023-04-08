package com.example.finalpr;



import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {
    public static FirebaseDatabase db;
    public static DatabaseReference myRef;
    public static List<Map<String,String>> fromDBList;
    public static List<Map<String,String>> fromDBListGames;
    public static List<Map<String,ArrayList<Comment>>> fromDBListGamesComments;
    public static List<Map<String,Long>> fromDBListNums;
    public static DatabaseReference databaseReference;
    //שם המפתחות של המשחקים ברשימה
    public static String[] gameKeys;
    //התגובות ברשימה
    public static String[][] commentsArray;
    // האתרים ברשימה
    public static String[][] sitesArray;
    // שמות האתרים ברשימה
    public static String[][] sitesNameArray;
    public FirebaseHelper(final String path){
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference(path);
        FirebaseHelper.myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int gameKeyIndex = 0, k=0, z=0;
                fromDBList = new ArrayList<>();
                fromDBListNums = new ArrayList<>();
                fromDBListGames = new ArrayList<>();
                fromDBListGamesComments = new ArrayList<>();
                commentsArray = new String[100][100];
                sitesArray = new String[100][100];
                sitesNameArray = new String[100][100];
                gameKeys = new String[100];
                for(DataSnapshot data : snapshot.getChildren()){
                    fromDBList.add((Map) data.getValue());
                    fromDBListNums.add((Map) data.getValue());
                    fromDBListGames.add((Map) data.getValue());
                    fromDBListGamesComments.add((Map) data.getValue());
                    //מוסיף למפתחות של המשחקים במקום הילד הנוכח את המפתח שלו
                    gameKeys[gameKeyIndex] =data.getKey();

                    for(DataSnapshot siteSnapshot : data.child("sitesArray").getChildren()) {
                        //עבור כל ילד במערך של האתרים מקבל את הערך\שם שלו ומציב בהתאם
                        String siteValue = String.valueOf(siteSnapshot.getValue());
                        sitesArray[gameKeyIndex][z]  = siteValue;
                        z++;
                    }
                    for(DataSnapshot siteSnapshot : data.child("sitesNameArray").getChildren()) {
                        //עבור כל ילד במערך של שמות האתרים מקבל את הערך\שם שלו ומציב בהתאם
                        String siteValue = String.valueOf(siteSnapshot.getValue());
                        sitesNameArray[gameKeyIndex][z]  = siteValue;
                        z++;
                    }
                    for(DataSnapshot commentSnapshot : data.child("comments").getChildren()){//.child(gameKeys[gameKeyIndex])
                        String commentValue = String.valueOf(commentSnapshot.getValue());
                        Log.d("COMMENTKEY", "commnet value1= "+ commentValue);
                        //מסיר מהטקסט את הקידומת של "commentUserName" כדי שנקבל את טקסט התגובה בלבד
                        int i = 17, j=0;
                        while(commentValue.charAt(i) != '=')
                            i++;
                        i++;
                        //קבלת טקסט התגובה עצמה
                        commentValue = commentValue.substring(i,commentValue.length()-1);
                        //מציב את תוכן התגובה במקום k במפתח משחק הנוכחי
                        commentsArray[gameKeyIndex][k] = commentValue;
                        Log.d("COMMENTKEY", "commnet value2= "+ commentsArray[gameKeyIndex][k]);
                                k++;
               }
                    gameKeyIndex++;
                }
                Log.d("FB", "FB connected and downloaded");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //יוצר רשימה של יוזרים עם הנתונים המותאמים
    public static List<User> convertToUserList(){
        List<User> userList = new ArrayList<>();
        for(int i =0;i< fromDBList.size();i++){
            User temp = new User(fromDBList.get(i).get("userName"),fromDBList.get(i).get("password"), fromDBListNums.get(i).get("age"),fromDBList.get(i).get("email"),Math.toIntExact(fromDBListNums.get(i).get("accessLevel")));
            userList.add(temp);
        }
        return userList;
    }
    //יוצר רשימה של משחקים עם הנתונים המותאמים
    public static List<GameObject> convertToGamesList(){
        List<GameObject> gameList = new ArrayList<>();
        for(int i =0;i< fromDBListGames.size();i++){
            GameObject temp = new GameObject(fromDBListGames.get(i).get("name"),fromDBListGames.get(i).get("gameImageURL"),fromDBListGames.get(i).get("description"));
            gameList.add(temp);
        }
        return gameList;
    }
        //התחברות למשתמש
    public static boolean login(String username, String password)
    {
        if(myRef !=null && LoggedInUser.dbUserList !=null) {
            for (User u : LoggedInUser.dbUserList) {
                if (u.getUserName().equals(username) && u.getPassword().equals(password)) {
                    LoggedInUser.loggedUser = u;
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    //בודק את שם המשתמש קיים בבסיס הנתונים
    public static boolean isExists(String name)
    {
        for (Map<String,String> map: fromDBList) {
            if(map.get("userName").equals(name))
                return true;
        }
        return false;
    }
    //בודק את שם המשתמש קיים בבסיס הנתונים עם שם, גיל ומייל
    public static String isExists(String name, double age, String mail)
    {
        for (int i=0; i< fromDBList.size();i++ ){
            Log.d("FB", "FB fromDBList loop");
            if(fromDBList.get(i).get("userName").equals(name) && fromDBListNums.get(i).get("age")==age && fromDBList.get(i).get("email").equals(mail))
                return fromDBList.get(i).get("password");
        }
        return null;
    }
    //רושם ומוסיף משתמש לבסיס הנותנים
    public static boolean register(User user)
    {
        if(myRef !=null) {
            try {
                //דוחף משתמש חדש לרשימת המשתמשים בבסיס הנתונים
                myRef.push().setValue(user);
                Log.e("FB", "Register success!");
                return true;
            }
            catch (Exception e) {
                Log.e("ERR", "Register failed!");
                return false;
            }}
        return false;
    }
    //רושם ומוסיף משחק לבסיס הנתונים
    public static boolean registerGame(GameObject obj)
    {
        if(myRef !=null) {
            try {
                //דוחף משחק חדש לרשימת המשחקים בבסיס הנתונים
                databaseReference =  myRef.push();
                databaseReference.setValue(obj);
                Log.e("FB", "Register success!");
                return true;
            }
            catch (Exception e) {
                Log.e("ERR", "Register failed!");
                return false;
            }}
        return false;
    }
    //מוסיף תגובה חדשה לתגובות במיקום של מפתח המשחק הנוכחי בבסיס הנתונים
    public static boolean registerComment(Comment comment, int keyIndex)
    {
        if(myRef !=null) {
            try {
                //משיג את המפתח הנוכחי של המשחק
                String keyName = gameKeys[keyIndex];
                //איפה שיש במשחק הזה ילד בשם "comments" דוחף ומוסיף את הערך החדש
               myRef.child(keyName).child("comments").push().setValue(comment);
                Log.e("FB", "Register success!");
                return true;
            }
            catch (Exception e) {
                Log.e("ERR", "Register failed!");
                return false;
            }}
        return false;
    }
    //עורך את שם המשחק
    public static void editGameName(int gameIndex, String newName)
    {
        String keyName = gameKeys[gameIndex];
        myRef.child(keyName).child("name").setValue(newName);
    }
    //עורך את תיאור המשחק
    public static void editGameDescription(int gameIndex, String newDecription)
    {
        String keyName = gameKeys[gameIndex];
        myRef.child(keyName).child("description").setValue(newDecription);
    }
    //עורך את תמונת המשחק
    public static void editGameImageURL(int gameIndex, String newImageURL)
    {
        String keyName = gameKeys[gameIndex];
        myRef.child(keyName).child("gameImageURL").setValue(newImageURL);
    }
    //מוסיף אתר למשחק
    public static void addGameWebsite(int gameIndex, String newWebsiteURL)
    {
        String keyName = gameKeys[gameIndex];
        myRef.child(keyName).child("sitesArray").push().setValue(newWebsiteURL);
    }
    //עורך את שם המשחק
    public static void addGameWebsiteName(int gameIndex, String newWebsiteURL)
    {
        String keyName = gameKeys[gameIndex];
        myRef.child(keyName).child("sitesNameArray").push().setValue(newWebsiteURL);
    }
        // מביא את התגובות של המשחק הנוכחי לרשימה
    public static ArrayList<String> getComments(int keyIndex) {
        ArrayList<String> comments = new ArrayList<>();
            try {
                for(int i =0;i<commentsArray.length;i++)
                comments.add(commentsArray[keyIndex][i]);
                Log.e("FB", "Register success!");
            } catch (Exception e) {
                Log.e("ERR", "Register failed!");
        }
       return comments;
    }
    // מביא את האתרים של המשחק הנוכחי לרשימה
    public static ArrayList<String> getSites(int keyIndex) {
        ArrayList<String> sites = new ArrayList<>();
        try {
            for(int i =0;i<sitesArray.length;i++)
                sites.add(sitesArray[keyIndex][i]);
            Log.e("FB", "Register success!");
            // return true;
        } catch (Exception e) {
            Log.e("ERR", "Register failed!");
            // return false;
        }
        return sites;
    }
    // מהביא את השם של המשחק הנוכחי
    public static ArrayList<String> getSitesName(int keyIndex) {
        ArrayList<String> sites = new ArrayList<>();
        try {
            for(int i =0;i<sitesNameArray.length;i++)
                sites.add(sitesNameArray[keyIndex][i]);
            Log.e("FB", "Register success!");
        } catch (Exception e) {
            Log.e("ERR", "Register failed!");
        }
        return sites;
    }
    //מוסיף קישור של אתר לרשימת האתרים במשחק הנוכחי
    public static boolean registerGameSites(String sitURL)
    {
        if(myRef !=null) {
            try {
                databaseReference.child("sitesArray").push().setValue(sitURL);
                Log.e("FB", "Register success!");
                return true;
            }
            catch (Exception e) {
                Log.e("ERR", "Register failed!");
                return false;
            }}
        return false;
    }
    //מוסיף את השם של האתר במשחק הנוכחי
    public static boolean registerGameSitesName(String sitURLName)
    {
        if(myRef !=null) {
            try {
                databaseReference.child("sitesNameArray").push().setValue(sitURLName);
                Log.e("FB", "Register success!");
                return true;
            }
            catch (Exception e) {
                Log.e("ERR", "Register failed!");
                return false;
            }}
        return false;
    }
    // מוחק את כל רשמחת המשחקים
    public static void deleteGamesList()
    {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("Games");
        mPostReference.removeValue();
    }
    //מוחק את המשחק שנבחר
    public static void deleteGame(int keyIndex)
    {
        FirebaseDatabase.getInstance().getReference().child("Games").child(gameKeys[keyIndex]).removeValue();
    }
}
