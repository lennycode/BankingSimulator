package com.grande.bank.bankingsimulator;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grande.bank.bankingsimulator.Utilities.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private Context context;
    private ArrayList<Account> AccountList;
    //java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(Locale.GERMAN);
    //Locale cLocale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
    DecimalFormat df2 = new DecimalFormat(".00");
    public AccountAdapter(Context context, ArrayList<Account> AccountList) {
        this.context = context;
        this.AccountList = AccountList;

    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.account_card_view, parent, false);

        AccountViewHolder AccountViewHolder = new AccountViewHolder(view);
        return AccountViewHolder;
    }



    @Override
    public void onBindViewHolder(final AccountViewHolder holder, int position) {
        Account Account;


        try {
            Account = AccountList.get(position);
            holder.txtAmount.setText(df2.format(Account.balance)+ Constants.euroSymbol);;
            holder.txtType.setText("Checking");
            holder.txtid.setText(Account.bk_id);
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        if (AccountList != null) {
            return AccountList.size();
        }
        return 0;
    }

    public List<Account> getAccounts() {
        return AccountList;
    }

    //ViewHolder class
    public class AccountViewHolder extends RecyclerView.ViewHolder {

        public CardView cvAccount;


        public TextView txtType;
        public TextView txtAmount;

        public TextView txtid;
        public AccountViewHolder(View AccountView) {
            super(AccountView);


            txtAmount = (TextView) AccountView.findViewById(R.id.txtAmount);
            txtType = (TextView) AccountView.findViewById(R.id.txtType);
            txtid = (TextView) AccountView.findViewById(R.id.id);

            AccountView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });


        }
    }

}
