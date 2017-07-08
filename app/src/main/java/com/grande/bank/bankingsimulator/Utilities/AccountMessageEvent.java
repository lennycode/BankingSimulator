package com.grande.bank.bankingsimulator.Utilities;


import com.grande.bank.bankingsimulator.Account;

import java.util.ArrayList;

public class AccountMessageEvent extends AccountEvent {


    public AccountMessageEvent(Boolean message,  ArrayList<Account> accounts) {
         super( message,    accounts);
    }
}

