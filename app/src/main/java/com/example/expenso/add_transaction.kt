package com.example.expenso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.expenso.databinding.ActivityAddTransactionBinding
import com.example.expenso.model.Transaction
import com.example.expenso.model.TransactionDatabase
import com.example.expenso.mvvm.TransViewModelFactory
import com.example.expenso.mvvm.TransactionRepository
import com.example.expenso.mvvm.TransactionViewModel

class add_transaction : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    private lateinit var viewModel: TransactionViewModel
    private var isIncome: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = TransactionDatabase.invoke(this).transactionDao()
        val repo = TransactionRepository(dao)
        val factory = TransViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)

        val radioGroup = binding.radioGroup
        val radioIncome = binding.RadioIncome
        val radioExpense = binding.RadioExpense

        binding.edLabel.addTextChangedListener {
            if (it!!.count() >= 0)
                binding.lytLabel.error = null
        }
        binding.edAmount.addTextChangedListener {
            if (it!!.count() >= 0)
                binding.lytAmount.error = null
        }
        radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                radioIncome.id -> {
                    isIncome = true
                    binding.btnsaveTrans.visibility = View.VISIBLE
                }

                radioExpense.id -> {
                    isIncome = false
                    binding.btnsaveTrans.visibility = View.VISIBLE
                }
                -1 ->{

                }
            }



        }
        addTransaction()
        exit()

    }
    fun addTransaction() {


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
                viewModel.insertTransaction(Transaction(0,label,amount.toDouble(),isIncome))
                Toast.makeText(this,"Transaction saved",Toast.LENGTH_SHORT).show()
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