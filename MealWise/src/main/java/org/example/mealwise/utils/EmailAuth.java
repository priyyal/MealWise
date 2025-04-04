package org.example.mealwise.utils;

public class EmailAuth {
    private static String email;

    public static void setEmail(String email){
        EmailAuth.email = email;
    }

    public static String getEmail(){
        return email;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
