package com.example.finalpr;

import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

public class GameObject {
    private String gameName;
    private String gameDescription;
    private String gameImageURL;
    private ArrayList<String> gameSites;
    private ArrayList<String> gameSitesName;
    private ArrayList<Comment> gameComments;
    public GameObject(String gameName, String gameImageURL, String gameDescription)
    {
        this.gameName = gameName;
        this.gameImageURL = gameImageURL;
        this.gameDescription = gameDescription;
        this.gameComments = new ArrayList<Comment>();
        this.gameSites = new ArrayList<String>();
        this.gameSitesName = new ArrayList<String>();
    }
    public String getName() {
        return gameName;
    }
    public String getDescription() {
        return gameDescription;
    }
    public ArrayList<Comment> getComments() {
        return gameComments;
    }
    public ArrayList<String> getGameSites() {
        return gameSites;
    }
    public String getGameImageURL() {
        return gameImageURL;
    }
    @Override
    public String toString() {
        return "GameObject{" +
                "gameName='" + gameName + '\'' +
                "gameImageURL='" + gameImageURL + '\'' +
                ", gameDescription='" + gameDescription + '\'' +
                ", gameComments=" + gameComments +
                '}';
    }
}
