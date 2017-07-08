package com.grande.bank.bankingsimulator.Utilities;



public class LoginMessageEvent {
    public final boolean outcome;
    public final int account;

    public LoginMessageEvent(Boolean message, int account) {
        this.outcome = message;
        this.account = account;
    }
}
