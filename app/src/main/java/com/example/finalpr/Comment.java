package com.example.finalpr;

public class Comment {
    private String commentUserName;
    private String commentText;
    //אובייקט של תגובה, מכיל טקסט של התגובה ואת שם הכותב
    public Comment(String commentText, String x)
    {
        //שם המגיב הוא המשתמש המחובר
        this.commentUserName = LoggedInUser.loggedUser.getUserName();
        this.commentText = commentText;
    }
    public String getCommentText() {
        return commentText;
    }
    public String getCommentUserName() {
        return commentUserName;
    }
    @Override
    public String toString() {
        return "Comment{" +
                "commentUserName='" + commentUserName + '\'' +
                ", commentText='" + commentText + '\'' +
                '}';
    }
}
