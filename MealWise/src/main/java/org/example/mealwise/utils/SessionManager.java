package org.example.mealwise.utils;

public class SessionManager {
    private static int userId;

    public static void setUserId(int userId){
        SessionManager.userId = userId;
    }

    public static int getUserId(){
        return userId;
    }
}
