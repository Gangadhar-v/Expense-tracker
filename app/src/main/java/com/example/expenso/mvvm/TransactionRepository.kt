package com.example.expenso.mvvm

import androidx.lifecycle.LiveData
import com.example.expenso.model.Transaction
import com.example.expenso.model.TransactionDao

class TransactionRepository(val transactionDao: TransactionDao){

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }
    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
    fun getAll(): LiveData<List<Transaction>>{
        return transactionDao.getAll()
    }

}