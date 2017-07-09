package com.grande.bank.bankingsimulator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.grande.bank.bankingsimulator.Utilities.AsyncResponse;
import com.grande.bank.bankingsimulator.Utilities.Constants;
import com.grande.bank.bankingsimulator.Utilities.DownloadFragment;
import com.grande.bank.bankingsimulator.Utilities.Session;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DecimalFormat df2 = new DecimalFormat(".00");
    TransactionAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    DownloadFragment mDownloadFragment;
    Spinner spinnerFrom;
    int countUserAccts = 0;
    ArrayAdapter<String> spinAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transaction, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mDownloadFragment = (DownloadFragment) getFragmentManager().findFragmentByTag(Constants.DF_TAG);
        if (mDownloadFragment == null) {
            mDownloadFragment = new DownloadFragment();
            getFragmentManager().beginTransaction().add(mDownloadFragment, Constants.DF_TAG).commit();
        }
        spinnerFrom = (Spinner) v.findViewById(R.id.spinAcctsfrom);
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = spinnerFrom.getSelectedItem().toString().split("-")[0];

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        return v;
    }



    private void initRecyclerView(ArrayList<Transaction> transactionList) {

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TransactionAdapter(getContext(), transactionList);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        new RequestBankingInfo(new AsyncResponse() {
            @Override
            public void processFinish(Object callback) {
                List<Account> accountMessageEvent = null;
                try {
                    accountMessageEvent = (ArrayList<Account>) callback;
                } catch (Exception e) {

                }
                if (accountMessageEvent == null){return;}

                List<String> spinnerArray = new ArrayList<String>();

                countUserAccts = accountMessageEvent.size();
                for (int i = 0; i < accountMessageEvent.size(); i++) {
                    Account ax = accountMessageEvent.get(i);
                    spinnerArray.add(ax.id + "-(" + (df2.format(ax.balance) + Constants.euroSymbol) + ")");

                }
               spinAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
                spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerFrom.setAdapter(spinAdapter);

            }
        }, mDownloadFragment

        ).getAcctByEmail(Session.email);

        new RequestBankingInfo(new AsyncResponse() {
            @Override
            public void processFinish(Object callback) {
                initRecyclerView((ArrayList<Transaction>)callback);
            }
        },mDownloadFragment
        ).requestTransactionByUser(Session.userUUID);

    }
}
