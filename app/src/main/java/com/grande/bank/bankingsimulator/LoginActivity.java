package com.grande.bank.bankingsimulator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grande.bank.bankingsimulator.Utilities.AppState;
import com.grande.bank.bankingsimulator.Utilities.AsyncResponse;
import com.grande.bank.bankingsimulator.Utilities.Constants;
import com.grande.bank.bankingsimulator.Utilities.DownloadFragment;
import com.grande.bank.bankingsimulator.Utilities.LoginMessageEvent;
import com.grande.bank.bankingsimulator.Utilities.RegistrationSuccessEvent;
import com.grande.bank.bankingsimulator.Utilities.Session;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mCardIdView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private DownloadFragment mDownloadFragment;
    private int mPasswordAttempts = 0;

    @Override
    protected void onRestart() {
        super.onRestart();
        if(Session.appState ==  AppState.JustRegistered){
            mCardIdView.setText(Session.cardId);
            Session.appState = AppState.LoggedOut;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDownloadFragment = (DownloadFragment) getSupportFragmentManager().findFragmentByTag(Constants.DF_TAG);
        if (mDownloadFragment == null) {
            mDownloadFragment = new DownloadFragment();
            getSupportFragmentManager().beginTransaction().add(mDownloadFragment, Constants.DF_TAG).commit();
        }

        // Set up the login form.
        mCardIdView = (AutoCompleteTextView) findViewById(R.id.email);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        final Button mLoginButton = (Button) findViewById(R.id.log_in_button);
        Button mRegisterButtom = (Button) findViewById(R.id.register_button);
        final Class mMainActicty = ControllerActivity.class;

        final Context mContext = this;


        mRegisterButtom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Session.appState = AppState.Register;
                Intent intent = new Intent(mContext, mMainActicty);
                startActivity(intent);



            }
        });


        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: add debounce(lock button so the user can't press again and cause issues)
                new RequestBankingInfo(
                        new AsyncResponse() {
                            @Override
                            public void processFinish(Object callback) {
                                //This will call verify login attempt thru the Eventbus
                            }
                        }, mDownloadFragment

                ).verifyLogin(mCardIdView.getText().toString(), mPasswordView.getText().toString());


                //attemptLogin();
                if (1 == 1) {

                    //Placeholder to pass things to the main activity
                    //String message = editText.getText().toString();
                    //intent.putExtra(EXTRA_MESSAGE, message);

                } else {


                }

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }





    /**
     * Check the login here, either show a message or move forward.
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void verifyLoginAttempt(LoginMessageEvent event) {
        if (event.outcome) { //succesfful attempt
            //Fetch user details
            new RequestBankingInfo(
                    new AsyncResponse() {
                        @Override
                        public void processFinish(Object callback) {
                            //
                        }
                    }, mDownloadFragment

            ).createBankingSession(event.account);
            //Wipe password
            mPasswordView.setText("");
            Session.appState = AppState.LoggedIn;
            Intent intent = new Intent(this, ControllerActivity.class);
               startActivity(intent);

            mPasswordAttempts = 0;
        } else{ //bad credentials
            Session.appState = AppState.LoggedOut;

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(getResources().getString(R.string.login_failure));
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }

            );

            mPasswordAttempts++;
            //Optional, lock out fields if we exceed Constants.passwordAttempts/
            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }




//************use this code to add validations...

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mCardIdView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mCardIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mCardIdView.setError(getString(R.string.error_field_required));
            focusView = mCardIdView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mCardIdView.setError(getString(R.string.error_invalid_email));
            focusView = mCardIdView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    /***********I am not using this, but I'll leave it for your information.
     /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

