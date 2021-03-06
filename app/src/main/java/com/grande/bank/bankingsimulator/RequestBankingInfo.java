package com.grande.bank.bankingsimulator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grande.bank.bankingsimulator.Utilities.AppState;
import com.grande.bank.bankingsimulator.Utilities.AsyncResponse;
import com.grande.bank.bankingsimulator.Utilities.Constants;
import com.grande.bank.bankingsimulator.Utilities.DownloadFragment;
import com.grande.bank.bankingsimulator.Utilities.LoginMessageEvent;
import com.grande.bank.bankingsimulator.Utilities.Session;
import com.grande.bank.bankingsimulator.Utilities.UserInfoMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;


public class RequestBankingInfo {
//CHECK ANDROID MONITOR TO SEE QUERIES RUN

    AsyncResponse asyncCallback;
    private DownloadFragment mDownloadFragment = null;


    public RequestBankingInfo(AsyncResponse asyncResponse) {
        asyncCallback = asyncResponse;
    }

    public RequestBankingInfo(AsyncResponse asyncResponse, DownloadFragment downloadFragment) {
        asyncCallback = asyncResponse;
        mDownloadFragment = downloadFragment;
    }

    public void requestTransactionByUser(String userId) {
        final String transactionRequest = Constants.dataEndpoint + String.format(Constants.user_transactions, userId);

        mDownloadFragment.DownloadFactory(new AsyncResponse() {
            @Override
            public void processFinish(Object callback) {
                String transactions = callback.toString();
                ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode node = objectMapper.readValue(transactions, JsonNode.class);
                    JsonNode brandNode = node.get("transactions");

                    for (int i = 0; i < brandNode.size(); i++) {
                        Transaction t = new Transaction();
                        JsonNode tNode = brandNode.get(i);
                        t.amount = Float.parseFloat(tNode.get("quantity").toString().replace("\"", ""));
                        t.datetime = tNode.get("timedate").toString().replace("\"", "");
                        t.ID = Integer.parseInt(tNode.get("id").toString().replace("\"", ""));
                        t.TransactionTypeReceiver = tNode.get("type_transaction_sender").toString().replace("\"", "");
                        t.TransactionTypeSender = tNode.get("type_transaction_receiver").toString().replace("\"", "");
                        //t.user_sender = tNode.get("id_sender").toString().replace("\"", "");
                        //t.user_receiver = tNode.get("id_receiver").toString().replace("\"", "");
                        t.user_sender = tNode.get("id_sender_account").toString().replace("\"", "");
                        t.user_receiver = tNode.get("id_receiver_account").toString().replace("\"", "");
                        transactionList.add(t);

                    }

                    String transaction = brandNode.asText();
                } catch (IOException e) {
                    //Send a failboy message back.
                    e.printStackTrace();
                    transactions = null;
                }


                asyncCallback.processFinish((Object) transactionList);
            }
        }).execute(transactionRequest);


    }

    public void createBankingSession(int id) {

        String loginRequest = Constants.dataEndpoint + String.format(Constants.user_information, id);
        mDownloadFragment.DownloadFactory(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                //Create set global variables for user information
                String transactions = output.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode node = objectMapper.readValue(transactions, JsonNode.class);
                    JsonNode userNode = node.get("user");
                    JsonNode user = userNode.get(0);
                    Session.email = user.get("email").toString().replace("\"", "");
                    Session.firstName = user.get("name").toString().replace("\"", "");
                    Session.lastName = user.get("surnames").toString().replace("\"", "");
                    Session.userUUID = user.get("id").toString().replace("\"", "");
                    Session.cardId = user.get("cardID").toString().replace("\"", "");
                    Session.isLoggedIn = true;
                    Session.appState = AppState.LoggedIn;

                } catch (IOException e) {
                    //Send a failboy message back.
                    e.printStackTrace();
                }

            }
        }).execute(loginRequest);
    }


    public void verifyLogin(String card, String pass) {
        String loginRequest = Constants.dataEndpoint + String.format(Constants.login_check, card, pass);
        mDownloadFragment.DownloadFactory(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String o = output.toString();
                if (o.equals("false")) {
                    //Eventbus is an alternative to using a direct callback, it is here for educational purposes
                    EventBus.getDefault().post(new LoginMessageEvent(false, 0));
                } else {
                    EventBus.getDefault().post(new LoginMessageEvent(true, Integer.parseInt(o)));
                }
            }
        }).execute(loginRequest);


    }

    public void fetchUserInfo(String id) {

    }


    public void regsterUser(String email, String pass, String cardId, String firstName, String lastName) {
        String loginRequest = Constants.dataEndpoint + String.format(Constants.register_user, email, pass, cardId, firstName, lastName);
        mDownloadFragment.DownloadFactory(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String o = output.toString();
                if (o.equals("false")) {
                    //Eventbus is an alternative to using a direct callback, it is here for educational purposes
                    EventBus.getDefault().post(new UserInfoMessageEvent(false));
                } else {
                    EventBus.getDefault().post(new UserInfoMessageEvent(true));
                }
            }
        }).execute(loginRequest);
    }


    public void getAccountInfo(String id) {


    }

    public void updateUser(String email, String pass, String cardId, String firstName, String lastName) {

        String loginRequest = Constants.dataEndpoint + String.format(Constants.update_user, Session.userUUID, email, pass, cardId, firstName, lastName);
        mDownloadFragment.DownloadFactory(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String o = output.toString();
                if (o.equals("false")) {
                    //Eventbus is an alternative to using a direct callback, it is here for educational purposes
                    EventBus.getDefault().post(new UserInfoMessageEvent(false));
                } else {
                    EventBus.getDefault().post(new UserInfoMessageEvent(true));
                }
            }
        }).execute(loginRequest);
    }

    public void getAcctByEmail(String email) {

        String loginRequest = Constants.dataEndpoint + String.format(Constants.accounts_by_email, email);
        mDownloadFragment.DownloadFactory(new AsyncResponse() {
            @Override
            public void processFinish(Object data) {
                String accounts = data.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                ArrayList<Account> accountList = null;
                try {

                    JsonNode node = objectMapper.readValue(accounts, JsonNode.class);
                    JsonNode brandNode = node.get("user");
                     accountList = new ArrayList<Account>();
                    for (int i = 0; i < brandNode.size(); i++) {
                        Account t = new Account();
                        JsonNode tNode = brandNode.get(i);
                        t.balance = Double.valueOf(tNode.get("balance").toString().replace("\"", ""));
                        t.id = tNode.get("id").toString().replace("\"", "");
                        t.user_id = tNode.get("user_id").toString().replace("\"", "");
                        t.pin = tNode.get("pin").toString().replace("\"", "");
                        t.bk_id = tNode.get("id_bank_account").toString().replace("\"", "");
                        accountList.add(t);
                    }
                    asyncCallback.processFinish(accountList);
                } catch (IOException e) {
                    //Send a failboy message back.
                    e.printStackTrace();
                }
                //This is a direct callback.
                asyncCallback.processFinish(accountList);
            }
        }).execute(loginRequest);
    }

    public void transferMoneytoUser(String senderAcct, String receiverAcct, String paymentType, String amount) {
        //Payment type is always 3

        String transferRequest = Constants.dataEndpoint + String.format(Constants.transfer_funds, senderAcct, receiverAcct, amount, paymentType);
        mDownloadFragment.DownloadFactory(new AsyncResponse() {
            @Override
            public void processFinish(Object data) {
                String accounts = data.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                boolean result;
                try {
                    result = (((String) data).equals("true") ? true : false);
                    asyncCallback.processFinish((Boolean) result);
                } catch (Exception e) {
                    //Send a failboy message back.
                    result = false;
                    e.printStackTrace();
                }
                asyncCallback.processFinish((Boolean) result);
            }
        }).execute(transferRequest);

    }


}


