package com.example.expenso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.expenso.databinding.ActivityUpdateTransactionBinding
import com.example.expenso.model.Transaction
import com.example.expenso.model.TransactionDatabase
import com.example.expenso.mvvm.TransViewModelFactory
import com.example.expenso.mvvm.TransactionRepository
import com.example.expenso.mvvm.TransactionViewModel
import kotlin.properties.Delegates

class update_transaction : AppCompatActivity() {
    private lateinit var binding:ActivityUpdateTransactionBinding
    private lateinit var viewModel :TransactionViewModel
    private var isIncome:Boolean = true
    private lateinit var transaction:Transaction
    private var id :Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transaction = intent.getSerializableExtra("transaction") as Transaction
        id = transaction.id

        binding.edLabel.setText(transaction.label)
        binding.edAmount.setText(transaction.amount.toString())
        if (transaction.isIncome) {
            binding.RadioIncome.isChecked = true
        } else {
            binding.RadioExpense.isChecked = true
        }





        val dao = TransactionDatabase.invoke(this).transactionDao()
        val repo = TransactionRepository(dao)
        val factory = TransViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)

        val radioGroup = binding.radioGroup
        val radioIncome = binding.RadioIncome
        val radioExpense = binding.RadioExpense
        radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                radioIncome.id -> {
                    isIncome = true
                }

                radioExpense.id -> {
                    isIncome = false
                }
            }
            binding.btnsaveTrans.visibility  = View.VISIBLE

        }
        binding.edAmount.addTextChangedListener {
            if(it!!.count()>0)
                binding.btnsaveTrans.visibility = View.VISIBLE
        }

        binding.edLabel.addTextChangedListener {
            if(it!!.count()>0)
                binding.btnsaveTrans.visibility = View.VISIBLE
        }
       updateTransaction()

        exit()
    }




    fun updateTransaction() {


        binding.btnsaveTrans.setOnClickListener {
            val label = binding.edLabel.text.toString().trim()
            val amount = binding.edAmount.text.toString().trim()

            if(label.isEmpty()){
                binding.lytLabel.error = "please enter label"
            }else{
                binding.lytLabel.error = null
            }

            if(amount.isEmpty()){
                binding.lytAmount.error = "please enter amount"
            }else{
                binding.lytAmount.error = null
            }

            if(label.isNotEmpty() && amount.isNotEmpty()){
                viewModel.updateTransaction(Transaction(id,label,amount.toDouble(),isIncome))
                Toast.makeText(this,"Transaction updated",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    fun exit(){
        binding.closebtn.setOnClickListener {
            finish()
        }
    }

}
