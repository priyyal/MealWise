package org.example.mealwise.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.regex.Pattern;

public class PasswordManager {

    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean validatePassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }

    public static String generateResetCode(){
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;  // Generate a 6-digit number
        return String.valueOf(code);
    }

    public static boolean isValidPasswordFormat(String input) {
        String regex = "^(?=.*\\d).{8,}$";
        return Pattern.matches(regex, input);
    }
}
