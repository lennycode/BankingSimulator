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
import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    DecimalFormat df2 = new DecimalFormat(".00");
    private Context context;
    private List<Transaction> transactionList;


    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;

    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.transaction_card_view, parent, false);

        TransactionViewHolder transactionViewHolder = new TransactionViewHolder(view);
        return transactionViewHolder;
    }


    @Override
    public void onBindViewHolder(final TransactionViewHolder holder, int position) {
        Transaction transaction;
        try {
            transaction = transactionList.get(position);

            String time = transaction.datetime.split(" ")[1];
            String date = transaction.datetime.split(" ")[0];
            String[] edate1 = date.split("-");
            String euroDate = edate1[2] + "/" + edate1[1] + "/" + edate1[0] + " " + time;

            holder.txtAmount.setText((df2.format(transaction.amount) + Constants.euroSymbol));
            holder.txtTransTo.setText(transaction.user_receiver);
            holder.txtTransDate.setText(euroDate);
            holder.txtTransFrom.setText(transaction.user_sender);
            holder.txtTransDate.setTag(transaction);


        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        if (transactionList != null) {
            return transactionList.size();
        }
        return 0;
    }

    public List<Transaction> getTransactions() {
        return transactionList;
    }

    //ViewHolder class
    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        public CardView cvTransaction;
        public TextView txtTransTo;
        public TextView txtTransFrom;
        public TextView txtAmount;
        public TextView txtTransDate;


        public TransactionViewHolder(View transactionView) {
            super(transactionView);
            transactionView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });

            txtTransDate = (TextView) transactionView.findViewById(R.id.txtTransDate);
            txtAmount = (TextView) transactionView.findViewById(R.id.txtAmount);
            txtTransTo = (TextView) transactionView.findViewById(R.id.txtTransTo);
            txtTransFrom = (TextView) transactionView.findViewById(R.id.txtTransFrom);
        }
    }

}
