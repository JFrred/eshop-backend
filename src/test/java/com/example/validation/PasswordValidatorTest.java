package com.example.validation;

import com.example.exception.InvalidPasswordException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {
    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void validPassword_ThenOk() {
        char[] valid0 = "Abc.123".toCharArray();
        char[] valid1 = "Xyz+987".toCharArray();

        String message = "Should not throw any exception, because password is valid";

        assertDoesNotThrow(() -> validator.validate(valid0, valid0), message);
        assertDoesNotThrow(() -> validator.validate(valid1, valid1), message);
    }

    @Test
    void passwordIsTooShort_ShouldThrowInvalidPasswordException() {
        char[] tooShort = "Ab.12".toCharArray();

        Exception exception = assertThrows(InvalidPasswordException.class,
                () -> validator.validate(tooShort, tooShort),
                "Should throw InvalidPasswordException, because password is too short");

        String actualMessage = exception.getMessage();
        String expectedMessage = "Password must be at least 6 characters long";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void passwordContainsSpaces_ShouldThrowInvalidPasswordException() {
        char[] withSpace = "Pass word1.".toCharArray();

        Exception exception = assertThrows(InvalidPasswordException.class,
                () -> validator.validate(withSpace, withSpace),
                "Should throw InvalidPasswordException, because password contains spaces");
        String actualMessage = exception.getMessage();
        String expectedMessage = "Password must not contain any spaces";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void passwordDoesNotContainAnyDigit_ShouldThrowInvalidPasswordException() {
        char[] noDigit = "PASS.word".toCharArray();

        Exception exception = assertThrows(InvalidPasswordException.class,
                () -> validator.validate(noDigit, noDigit),
                "Should throw InvalidPasswordException, because password does not contain any digit");
        String actualMessage = exception.getMessage();
        String expectedMessage = "Password must consist of at least one digit";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void passwordDoesNotContainAnyLowerCaseLetter_ShouldThrowInvalidPasswordException() {
        char[] noLowerCase = "PASS.WORD1".toCharArray();

        Exception exception = assertThrows(InvalidPasswordException.class,
                () -> validator.validate(noLowerCase, noLowerCase),
                "Should throw InvalidPasswordException, because password does not contain any lower case letter");
        String actualMessage = exception.getMessage();
        String expectedMessage = "Password must consist of at least one lower case letter";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void passwordDoesNotContainAnyUpperCaseLetter_ShouldThrowInvalidPasswordException() {
        char[] noUpperCase = "pass.word1".toCharArray();

        Exception exception = assertThrows(InvalidPasswordException.class,
                () -> validator.validate(noUpperCase, noUpperCase),
                "Should throw InvalidPasswordException, because password does not contain any upper case letter");
        String actualMessage = exception.getMessage();
        String expectedMessage = "Password must consist of at least one upper case letter";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void passwordDoesNotContainAnySpecialCharacter_ShouldThrowInvalidPasswordException() {
        char[] noSpecialChar = "Password1".toCharArray();

        Exception exception = assertThrows(InvalidPasswordException.class,
                () -> validator.validate(noSpecialChar, noSpecialChar),
                "Should throw InvalidPasswordException, because password does not contain any special character");
        String actualMessage = exception.getMessage();
        String expectedMessage = "Password must contain at least one special character";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void passwordsAreNotTheSame_ShouldThrowInvalidPasswordException() {
        char[] valid0 = "Abc.123".toCharArray();
        char[] valid1 = "Xyz+987".toCharArray();

        Exception exception = assertThrows(InvalidPasswordException.class,
                () -> validator.validate(valid0, valid1),
                "Should throw InvalidPasswordException, because passwords are not the same");
        String actualMessage = exception.getMessage();
        String expectedMessage = "Passwords are not the same";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}