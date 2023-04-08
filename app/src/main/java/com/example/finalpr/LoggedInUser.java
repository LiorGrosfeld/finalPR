package com.example.finalpr;

import java.util.ArrayList;
import java.util.List;

public class LoggedInUser {
    public static User loggedUser;
    public static List<User> dbUserList;
    public static List<String> dbUserSt;
    public static void prepare()
    {
        dbUserList = FirebaseHelper.convertToUserList();
    }
    public static void prepareSt()
    {
        dbUserSt = new ArrayList<>();
        if(dbUserList != null)
        {
            for (User u: dbUserList)
            {
                String temp = u.getUserName()+":"+u.getPassword();
                temp += u.getAccessLevel() == 1 ? " (Admin)":"";
                dbUserSt.add(temp);
            }
        }
    }
}
