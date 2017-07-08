package com.grande.bank.bankingsimulator.Utilities;

/**
 * Created by lenny on 7/7/17.
 */

public class UserInfoMessageEvent {
    public final boolean outcome;


    public  UserInfoMessageEvent(Boolean message ) {
        this.outcome = message;

    }
}