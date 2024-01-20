package com.example.expenso.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database([Transaction::class],version=1)
abstract class TransactionDatabase:RoomDatabase() {
    abstract fun transactionDao():TransactionDao

    //all instance of TransactionDatabase class, this remains same
    companion object {

        //this make the thread immediately visible to other thread
        @Volatile
        private var instance:TransactionDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance?: synchronized(LOCK){
            instance ?: getDatabase(context).also{
                instance = it
            }
        }


    private fun getDatabase(context:Context):TransactionDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            TransactionDatabase::class.java,
         "transaction_database"
    ).build()

   }
}