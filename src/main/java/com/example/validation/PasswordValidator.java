package com.example.validation;

import com.example.exception.InvalidPasswordException;
import org.springframework.stereotype.Component;

import static java.lang.Character.*;

@Component
public class PasswordValidator {

    public void validate(char[] password, char[] confirmPassword) {
        containsDigit(password);
        containsLowerCaseLetter(password);
        containsUpperCaseLetter(password);
        containsSpecialChar(password);
        longEnough(password);
        noSpaces(password);
        passwordsTheSame(password, confirmPassword);
    }

    private void longEnough(char[] password) {
        if (password.length < 6)
            throw new InvalidPasswordException(
                    "Password must be at least 6 characters long");
    }

    private void noSpaces(char[] password) {
        for (char c : password)
            if (isSpaceChar(c))
                throw new InvalidPasswordException(
                        "Password must not contain any spaces");
    }

    private void containsDigit(char[] password) {
        boolean hasDigit = false;
        for (char c : password)
            if (isDigit(c))
                hasDigit = true;

        if (!hasDigit)
            throw new InvalidPasswordException(
                    "Password must consist of at least one digit");
    }

    private void containsLowerCaseLetter(char[] password) {
        boolean hasLowerCase = false;

        for (char c : password)
            if (isLowerCase(c))
                hasLowerCase = true;

        if (!hasLowerCase)
            throw new InvalidPasswordException
                    ("Password must consist of at least one lower case letter");
    }

    private void containsUpperCaseLetter(char[] password) {
        boolean hasUpperCase = false;

        for (char c : password)
            if (isUpperCase(c))
                hasUpperCase = true;

        if (!hasUpperCase)
            throw new InvalidPasswordException(
                    "Password must consist of at least one upper case letter");
    }

    private void containsSpecialChar(char[] password) {
        boolean hasSpecialChar = false;

        for (char c : password)
            for (char cs : specialChars())
                if (c == cs) {
                    hasSpecialChar = true;
                    break;
                }

        if (!hasSpecialChar)
            throw new InvalidPasswordException(
                    "Password must contain at least one special character");
    }

    private char[] specialChars() {
        return new char[] {
                '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
                '-', '_', '=', '+', '[', '{', ']', '}', '\\', '|', '\'',
                '\"', ';', ':', '/', '?', '.', '>', ',', '<'
        };
    }

    private void passwordsTheSame(char[] password0, char[] password1) {
        for (int i = 0; i < password1.length; i++)
            if (password0[i] != password1[i])
                throw new InvalidPasswordException("Passwords are not the same");
    }

}

