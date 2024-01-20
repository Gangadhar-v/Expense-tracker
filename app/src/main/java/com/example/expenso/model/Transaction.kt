package com.example.expenso.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity("transaction_table")
data class Transaction(

    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val label:String,
    val amount:Double,
    val isIncome:Boolean
):Serializable
