package com.example.expenso

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.expenso.model.Transaction

class TransactionAdapter(val context: Context, val transactions:List<Transaction>):
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(item: View):ViewHolder(item){
        val label = item.findViewById<TextView>(R.id.tvlabel)
        val amount = item.findViewById<TextView>(R.id.tvamount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {

        val item = LayoutInflater.from(context).inflate(R.layout.transaction,parent,false)
        return TransactionViewHolder(item)
    }

    override fun getItemCount(): Int {
       return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {

        val transaction = transactions[position]
        var context = holder.amount.context
        val label = transaction.label


        holder.amount.text = transaction.amount.toString()
        if(transaction.isIncome == true){
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.green))

        }else{
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.red))

        }

        holder.label.text = label

        holder.itemView.setOnClickListener {
            val intent = Intent(context,update_transaction::class.java)
            intent.putExtra("transaction",transactions[position])
            context.startActivity(intent)
        }




    }



}