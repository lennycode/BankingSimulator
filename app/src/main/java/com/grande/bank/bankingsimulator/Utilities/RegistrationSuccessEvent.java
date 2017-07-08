package com.grande.bank.bankingsimulator.Utilities;

/**
 * Created by lenny on 7/7/17.
 */

public class RegistrationSuccessEvent {
    public final boolean outcome;


    public RegistrationSuccessEvent(Boolean message ) {
        this.outcome = message;

    }
}
