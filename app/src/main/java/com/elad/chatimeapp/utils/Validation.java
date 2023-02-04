package com.elad.chatimeapp.utils;

import android.util.Patterns;

/**
 * @author - Elad Sabag
 * @date - 1/27/2023
 */
public class Validation {
    public static boolean isPhoneNumberValid(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }
}
