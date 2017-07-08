package com.grande.bank.bankingsimulator.Utilities;

/**
 * Created by lenny on 7/5/17.
 */

//These are constants that are modified by the Login Activitt
public class Session {
    public static String email;
    public static String firstName;
    public static String lastName;
    public static String userUUID;
    public static String cardId;

    public static AppState appState = AppState.LoggedOut;
    public  static Boolean isLoggedIn = false;
}
