package com.grande.bank.bankingsimulator;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.grande.bank.bankingsimulator.Utilities.AsyncResponse;
import com.grande.bank.bankingsimulator.Utilities.Constants;
import com.grande.bank.bankingsimulator.Utilities.DownloadFragment;
import com.grande.bank.bankingsimulator.Utilities.Session;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Spinner spinnerFrom;
    Spinner spinnerTo;
    EditText amount;
    DownloadFragment mDownloadFragment;
    boolean userAcctsPopulated = false;
    int countUserAccts = 0;
    AutoCompleteTextView email;
    DecimalFormat df2 = new DecimalFormat(".00");
    Button fetchAccounts, submitTransfer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransferFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransferFragment newInstance(String param1, String param2) {
        TransferFragment fragment = new TransferFragment();
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

        mDownloadFragment = (DownloadFragment) getFragmentManager().findFragmentByTag(Constants.DF_TAG);
        if (mDownloadFragment == null) {
            mDownloadFragment = new DownloadFragment();
            getFragmentManager().beginTransaction().add(mDownloadFragment, Constants.DF_TAG).commit();
        }
        View v = inflater.inflate(R.layout.fragment_transfer, container, false);
        fetchAccounts = (Button) v.findViewById(R.id.selaccts);
        spinnerFrom = (Spinner) v.findViewById(R.id.spinAcctsfrom);
        spinnerTo = (Spinner) v.findViewById(R.id.spinAcctsto);
        submitTransfer = (Button) v.findViewById(R.id.btnSubmitTransfer);
        email = (AutoCompleteTextView) v.findViewById(R.id.email);
        amount = (EditText) v.findViewById(R.id.amount);
        submitTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Double.parseDouble(amount.getText().toString()) > 0 && !email.getText().toString().equals("")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage(String.format(("Confirm Transfer of %s \nFrom acct %s \nTo acct %s \nBelonging to %s"), amount.getText().toString(),
                            spinnerFrom.getSelectedItem().toString().split("-")[0],
                            spinnerTo.getSelectedItem().toString(),
                            email.getText().toString()
                    ));
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

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
        });


        fetchAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().trim().equals("")) {

                    new RequestBankingInfo(new AsyncResponse() {
                        @Override
                        public void processFinish(Object callback) {
                            ArrayList<Account> accountMessageEvent = null;
                            if (accountMessageEvent == null) return;
                            try {
                                accountMessageEvent = (ArrayList<Account>) callback;
                            } catch (Exception e) {

                            }
                            List<String> spinnerArray = new ArrayList<String>();

                            if (countUserAccts > 0) {
                                countUserAccts = accountMessageEvent.size();
                                for (int i = 0; i < accountMessageEvent.size(); i++) {
                                    Account ax = accountMessageEvent.get(i);
                                    //Don't show other users money!
                                    // spinnerArray.add(ax.id + "-(" + (df2.format(ax.balance) + "\u20ac") + ")");
                                    //No amounts shown!
                                    spinnerArray.add(ax.id);
                                    userAcctsPopulated = true;
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                spinnerTo.setAdapter(adapter);
                            }


                        }
                    }, mDownloadFragment).getAcctByEmail(Session.email);
                } else {

                    //complain about no email...
                }
            }
        });


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        new RequestBankingInfo(new AsyncResponse() {
            @Override
            public void processFinish(Object callback) {

                ArrayList<Account> accountMessageEvent = null;

                try {
                    accountMessageEvent = (ArrayList<Account>) callback;
                } catch (Exception e) {

                }
                List<String> spinnerArray = new ArrayList<String>();
                if (!userAcctsPopulated) {//First time, populate top spinner
                    countUserAccts = accountMessageEvent.size();
                    for (int i = 0; i < accountMessageEvent.size(); i++) {
                        Account ax = accountMessageEvent.get(i);
                        spinnerArray.add(ax.id + "-(" + (df2.format(ax.balance) + "\u20ac") + ")");
                        userAcctsPopulated = true;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnerFrom.setAdapter(adapter);
                }

            }
        }, mDownloadFragment

        ).getAcctByEmail(Session.email);
    }

    //mySpinner.getSelectedItem().toString()


}
