package org.example.mealwise.services;

import org.example.mealwise.models.User;
import org.example.mealwise.dao.UserDAO;
import org.example.mealwise.utils.EmailAuth;
import org.example.mealwise.utils.EmailService;
import org.example.mealwise.utils.PasswordManager;
import org.example.mealwise.utils.SessionManager;

public class UserService {
    private static final UserDAO userDAO = new UserDAO();
    private static String resetCode;

    public static boolean createAccount(String name, String email, String password){
        boolean isCreated = false;
        if (userDAO.getUserByEmail(email) == null){
            User newUser = new User(name, email, PasswordManager.hashPassword(password));
            userDAO.insert(newUser);
            isCreated = true;
        }
        return isCreated;
    }

    public static boolean login(String text, String password){
        if (EmailAuth.isValidEmail(text)){
            return logInByEmail(text, password);
        }
        return logInByName(text, password);
    }

    private static boolean logInByEmail(String email, String password){
        boolean isLoggedIn = false;
        User user = userDAO.getUserByEmail(email);
        if (user != null){
            isLoggedIn = PasswordManager.validatePassword(password, user.getPasswordHash());
            if (isLoggedIn){
                SessionManager.setUserId(user.getUserId());
            }
        }
        return isLoggedIn;
    }

    private static boolean logInByName(String username, String password){
        boolean isLoggedIn = false;
        User user = userDAO.getUserByName(username);
        if (user != null){
            isLoggedIn = PasswordManager.validatePassword(password, user.getPasswordHash());
            if (isLoggedIn){
                SessionManager.setUserId(user.getUserId());
                System.out.println(SessionManager.getUserId());
            }
        }
        return isLoggedIn;
    }

    public static boolean initiatePasswordReset(String email){
        boolean isInitiated = false;
        User user = userDAO.getUserByEmail(email);
        if (user != null){
            resetCode = PasswordManager.generateResetCode();
            EmailService.sendPasswordResetEmail(email, resetCode);
            isInitiated = true;
        }
        return isInitiated;
    }

    public static boolean verifyOTP(String otp){
        return resetCode.equals(otp);
    }

    public static boolean resetPassword(String email, String password){
        return userDAO.updateUserPassword(PasswordManager.hashPassword(password), email);
    }

}