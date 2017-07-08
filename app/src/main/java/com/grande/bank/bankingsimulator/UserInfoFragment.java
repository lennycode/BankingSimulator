package com.grande.bank.bankingsimulator;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grande.bank.bankingsimulator.Utilities.AppState;
import com.grande.bank.bankingsimulator.Utilities.AsyncResponse;
import com.grande.bank.bankingsimulator.Utilities.Constants;
import com.grande.bank.bankingsimulator.Utilities.DownloadFragment;
import com.grande.bank.bankingsimulator.Utilities.Session;
import com.grande.bank.bankingsimulator.Utilities.UserInfoMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText cardNumber;
    Button signButton;
    TextView actionLabel;
    DownloadFragment mDownloadFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInfoFragment newInstance(String param1, String param2) {
        UserInfoFragment fragment = new UserInfoFragment();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.userinfo_fragment, container, false);
        mDownloadFragment = (DownloadFragment) getFragmentManager().findFragmentByTag(Constants.DF_TAG);
        if (mDownloadFragment == null) {
            mDownloadFragment = new DownloadFragment();
            getFragmentManager().beginTransaction().add(mDownloadFragment, Constants.DF_TAG).commit();
        }
        firstName = (EditText) v.findViewById(R.id.fname);
        lastName = (EditText) v.findViewById(R.id.lname);
        actionLabel = (TextView) v.findViewById(R.id.action_type);
        email = (EditText) v.findViewById(R.id.email);
        password = (EditText) v.findViewById(R.id.password);
        cardNumber = (EditText) v.findViewById(R.id.card_number);
        signButton = (Button) v.findViewById(R.id.email_signup_button);
        if (Session.appState == AppState.LoggedIn) {
            firstName.setText(Session.firstName);
            lastName.setText(Session.lastName);
            email.setText(Session.email);
            cardNumber.setText(Session.cardId);


            actionLabel.setText("Please Edit your information!");

            signButton.setText("Confirm your changes!");
        } else {

        }

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Session.appState == AppState.LoggedIn) {
                    new RequestBankingInfo(
                            new AsyncResponse() {
                                @Override
                                public void processFinish(Object callback) {

                                }
                            }, mDownloadFragment

                    ).updateUser(email.getText().toString(), password.getText().toString(), cardNumber.getText().toString(), firstName.getText().toString(), lastName.getText().toString());

                } else if (Session.appState == AppState.Register) {
                    new RequestBankingInfo(
                            new AsyncResponse() {
                                @Override
                                public void processFinish(Object callback) {
                                    //This will call verify login attempt thru the Eventbus
                                }
                            }, mDownloadFragment

                    ).regsterUser(email.getText().toString(), password.getText().toString(), cardNumber.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
                }
            }
        });


        return v;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userInfoResut(UserInfoMessageEvent userInfoMessageEvent) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        if (Session.appState == AppState.LoggedIn) {
            builder1.setMessage(("Information Updated"));

        } else if (Session.appState == AppState.Register) {

            builder1.setMessage(("Congratulations, you are registered!"));

        }
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Session.appState = AppState.JustRegistered;
                        Session.cardId = cardNumber.getText().toString();
                        //  EventBus.getDefault().postSticky(new RegistrationSuccessEvent(true));
                        getActivity().finish();
                    }
                }

        );


        AlertDialog alert11 = builder1.create();
        alert11.show();

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
