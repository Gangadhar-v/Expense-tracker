package com.example.expenso.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertTransaction(transaction:Transaction)

    @Update
    suspend fun updateTransaction(transaction:Transaction)

    @Delete
    suspend fun deleteTransaction(transaction:Transaction)

    @Query("select * from transaction_table")
    fun getAll():LiveData<List<Transaction>>
}