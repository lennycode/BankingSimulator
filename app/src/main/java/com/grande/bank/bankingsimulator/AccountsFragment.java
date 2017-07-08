package com.grande.bank.bankingsimulator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grande.bank.bankingsimulator.Utilities.AsyncResponse;
import com.grande.bank.bankingsimulator.Utilities.Constants;
import com.grande.bank.bankingsimulator.Utilities.DownloadFragment;
import com.grande.bank.bankingsimulator.Utilities.Session;

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


        initRecyclerView(accounts);

        return v;
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
    public void onStart() {
        super.onStart();
        new RequestBankingInfo(new AsyncResponse() {
            @Override
            public void processFinish(Object callback) {
                try {
                    accounts = (ArrayList<Account>) callback;
                    initRecyclerView(accounts);
                }catch(Exception e){

                }

            }
        }, mDownloadFragment

        ).getAcctByEmail(Session.email);
    }
}
