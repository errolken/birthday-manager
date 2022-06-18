package com.birthdaymanager.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    String nameRegEx = "^(.){6,}$";
    String passwordRegEx = "^(.){6,}$";
    String emailRegEx = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    public boolean validateName(String name) {
        Pattern pattern = Pattern.compile(nameRegEx);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(passwordRegEx);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
