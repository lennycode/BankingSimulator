package com.grande.bank.bankingsimulator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.grande.bank.bankingsimulator.Utilities.AppState;
import com.grande.bank.bankingsimulator.Utilities.Constants;
import com.grande.bank.bankingsimulator.Utilities.Session;

public class ControllerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        TextView userNameText = (TextView) hView.findViewById(R.id.textUser);

        if (Session.appState == AppState.LoggedIn) {
            //Just in case, because we are locking it for registration.
            ft.replace(R.id.fragment_container, new AccountsFragment(), Constants.ACCOUNTS_FRAGMENT).commit();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawer.openDrawer(Gravity.LEFT);
            userNameText.setText(getResources().getString(R.string.Welcomes) + " " + (Session.firstName + " " + Session.lastName).replace("\"", ""));




        } else if (Session.appState == AppState.Register) {
            //During registration, the main drawer is locked!
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            ft.replace(R.id.fragment_container, new UserInfoFragment(), Constants.USERINFO_FRAGMENT).commit();
        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            final Class lac = LoginActivity.class;
            final Activity act = this;
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage( "Would you like to log out?");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Session.appState = AppState.LoggedOut;
                            Session.isLoggedIn = false;
                            Intent intent = new Intent(act, lac);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
            );
            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }

            );
            //Optional, lock out fields if we exceed Constants.passwordAttempts/
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.acct_balance) {
            ft.replace(R.id.fragment_container, new AccountsFragment(), Constants.ACCOUNTS_FRAGMENT).commit();
        } else if (id == R.id.acct_logout) {
            Session.appState = AppState.LoggedOut;
            Session.isLoggedIn = false;
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else if (id == R.id.acct_pass) {

        } else if (id == R.id.acct_user_info) {
            ft.replace(R.id.fragment_container, new UserInfoFragment(), Constants.USERINFO_FRAGMENT).commit();

        } else if (id == R.id.acct_transfer) {
            ft.replace(R.id.fragment_container, new TransferFragment(), Constants.TRANSFER_FRAGMENT).commit();


        } else if (id == R.id.acct_transactions) {
            ft.replace(R.id.fragment_container, new TransactionFragment(), Constants.TRANSACTION_FRAGMENT).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
