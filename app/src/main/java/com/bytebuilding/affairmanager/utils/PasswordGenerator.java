package com.bytebuilding.affairmanager.utils;

import java.security.SecureRandom;

/**
 * Created by Turkin A. on 11.05.17.
 */

public class PasswordGenerator {

    public static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static SecureRandom secureRandom;

    public static String newGeneratedPassword() {
        secureRandom = new SecureRandom();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            stringBuilder.append(ALPHABET.charAt(secureRandom.nextInt(ALPHABET.length())));
        }

        return stringBuilder.toString();
    }

}
