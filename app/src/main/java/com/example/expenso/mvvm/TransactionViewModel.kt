package com.example.expenso.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenso.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TransactionViewModel(val repo:TransactionRepository):ViewModel() {

    private val transactionEventChannel= Channel<TransactionEvent>()
    val transactionEvent = transactionEventChannel.receiveAsFlow()
    val inputLabel = MutableLiveData<String>()
    val inputAmount = MutableLiveData<String>()
    val inputIsincome = MutableLiveData<Boolean>()

    fun addTransaction(){
        val label = inputLabel.value
        val amount = inputAmount.value.toString().toDouble()
        val isIncome = inputIsincome.value
        insertTransaction(Transaction(0,label!!,amount!!,isIncome!!))
    }

    fun insertTransaction(transaction: Transaction){
        viewModelScope.launch {
            Dispatchers.IO
            repo.insertTransaction(transaction)
        }
    }

    fun updateTransaction(transaction:Transaction){
        viewModelScope.launch {
            Dispatchers.IO
            repo.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction:Transaction){
        viewModelScope.launch {
            Dispatchers.IO
            repo.deleteTransaction(transaction)
            transactionEventChannel.send(TransactionEvent.ShowUndoDeleteMessage(transaction))
        }
    }

    val getAll = repo.getAll()

    sealed class TransactionEvent{
        data class ShowUndoDeleteMessage(val transaction:Transaction):TransactionEvent()
    }

    fun onClickUndoDelete(transaction:Transaction){
        viewModelScope.launch { insertTransaction(transaction) }
    }
}