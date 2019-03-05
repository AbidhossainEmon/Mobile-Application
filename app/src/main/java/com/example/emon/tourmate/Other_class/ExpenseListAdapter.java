package com.example.emon.tourmate.Other_class;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.emon.tourmate.R;

import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.MyViewHoldwer> {
    public ExpenseListAdapter(List<Expense> expenses, Context context) {
        this.expenses = expenses;
        this.context = context;
    }

    private List<Expense> expenses;
    Context context;
    @NonNull
    @Override
    public MyViewHoldwer onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.expense_item,viewGroup,false);
        MyViewHoldwer viewHoldwer=new MyViewHoldwer(view);
        return viewHoldwer;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoldwer myViewHoldwer, int i) {
        myViewHoldwer.descriptionET.setText(expenses.get(i).getDescription());
        myViewHoldwer.amountET.setText(String.valueOf(expenses.get(i).getAmount()));


    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public  class MyViewHoldwer extends RecyclerView.ViewHolder {
        TextView descriptionET,amountET;
        public MyViewHoldwer(@NonNull View itemView) {
            super(itemView);
            descriptionET=itemView.findViewById(R.id.descriptionETID);
            amountET=itemView.findViewById(R.id.amountETID);
        }
    }
}
