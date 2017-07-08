package com.grande.bank.bankingsimulator.Utilities;

import com.grande.bank.bankingsimulator.Transaction;

import java.util.ArrayList;

/**
 * Created by lenny on 7/6/17.
 */

public class TransactionMessageEvent {


        public   boolean outcome;
        public   ArrayList<Transaction> aTx;

        public void LoginMessageEvent(Boolean message, ArrayList<Transaction> aTx) {
            this.outcome = message;
            this.aTx = aTx;
        }


}
