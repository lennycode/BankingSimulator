package com.grande.bank.bankingsimulator.Utilities;

import android.app.Application;
import android.content.res.Resources;

/**
 * Created by lenny on 7/4/17.
 */

public class Constants {

    public static  int passwordAttempts = 3;
    public  static final String DF_TAG = "download_fragment";
    public static final String dataEndpoint = "http://192.168.2.3:8080/banking/webservices.php?f=";
    public static final String user_transactions = "getPaymentHistoryByUser?user=%s";
    public static final String user_information = "getUserInfo&user=%s";
    public static final String login_check = "doLogin&cardID=%s&password=%s";
    public static final String register_user = "doRegister&email=%s&password=%s&cardID=%s&name=%s&surnames=%s";
    public static final String update_user = "doUpdateUserInfo&user=%s&email=%s&password=%s&cardID=%s&name=%s&surnames=%s";
    public static final String accounts_by_email = "getUserBankAccountsByEmail&email=%s";
    //Android likes unique tags for fragments so they can be easily located after they are created.
    public static final String TRANSFER_FRAGMENT= "transfer_fragment";
    public static final String TRANSACTION_FRAGMENT= "transaction_fragment";
    public static final String USERINFO_FRAGMENT= "userinfo_fragment";
    public static final String ACCOUNTS_FRAGMENT= "accounts_fragment";
}
