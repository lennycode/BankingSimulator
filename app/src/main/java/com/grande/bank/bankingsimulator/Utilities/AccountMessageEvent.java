package com.grande.bank.bankingsimulator.Utilities;


import com.grande.bank.bankingsimulator.Account;

import java.util.ArrayList;

public class AccountMessageEvent {
    public   boolean outcome;
    public   ArrayList<Account> accounts;

    public AccountMessageEvent(Boolean message,  ArrayList<Account> accounts) {
        this.outcome = message;
        this.accounts = accounts;
    }
}

