package com.grande.bank.bankingsimulator;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private Context context;
    private List<Transaction> transactionList;
//    private Picasso picasso;
//    private OkHttpClient okHttpClient;

    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;

    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        TransactionViewHolder transactionViewHolder = new TransactionViewHolder(view);
        return transactionViewHolder;
    }



    @Override
    public void onBindViewHolder(final TransactionViewHolder holder, int position) {
        Transaction transaction;
        try {
            transaction = transactionList.get(position);

//            holder.txtAmount.setText( transaction.transactionAmount);
//            holder.txtPurchaseTime.setText(transaction.purchaseDate+"\n"+transaction.purchaseTime);
//            holder.txtStore.setText(transaction.Store + "\n"+transaction.Address);
//            holder.txtType.setText(transaction.TransactionType);



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
        public boolean isShoppingList;
        public CardView cvTransaction;
        public ImageView ivImg;
        public TextView txtStore;
        public TextView txtType;
        public TextView txtAmount;
        public ImageView ivbargain;
        public TextView txtPurchaseTime;

        public TransactionViewHolder(View transactionView) {
            super(transactionView);
            transactionView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!isShoppingList) {

                    }
                }
            });

//            txtPurchaseTime = (TextView) transactionView.findViewById(R.id.txtPurchaseTime);
//            txtAmount = (TextView) transactionView.findViewById(R.id.txtAmount);
//            txtStore = (TextView) transactionView.findViewById(R.id.txtStore);
//            txtType = (TextView) transactionView.findViewById(R.id.txtType);
        }
    }

}
