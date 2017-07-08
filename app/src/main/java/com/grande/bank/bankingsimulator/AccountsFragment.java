package com.grande.bank.bankingsimulator;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grande.bank.bankingsimulator.Utilities.AccountMessageEvent;
import com.grande.bank.bankingsimulator.Utilities.AppState;
import com.grande.bank.bankingsimulator.Utilities.AsyncResponse;
import com.grande.bank.bankingsimulator.Utilities.Constants;
import com.grande.bank.bankingsimulator.Utilities.DownloadFragment;
import com.grande.bank.bankingsimulator.Utilities.Session;
import com.grande.bank.bankingsimulator.Utilities.UserInfoMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountsFragment extends Fragment {
    DownloadFragment mDownloadFragment;
    AccountAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<Account> accounts = new ArrayList<>();
    public AccountsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDownloadFragment = (DownloadFragment) getFragmentManager().findFragmentByTag(Constants.DF_TAG);
        if (mDownloadFragment == null) {
            mDownloadFragment = new DownloadFragment();
            getFragmentManager().beginTransaction().add(mDownloadFragment, Constants.DF_TAG).commit();
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accounts, container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.acct_recycler_view);

        new RequestBankingInfo(new AsyncResponse() {
            @Override
            public void processFinish(Object callback) {

            }
        }, mDownloadFragment

        ).getAcctByEmail(Session.email);
        initRecyclerView(accounts);

        return v;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userInfoResut(AccountMessageEvent accountMessageEvent) {
        accounts = accountMessageEvent.accounts;
         initRecyclerView(accounts);

    }

    private void initRecyclerView(ArrayList<Account> accountList) {

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AccountAdapter(getContext(), accountList);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

}
