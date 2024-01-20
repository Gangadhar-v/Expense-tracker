package com.example.expenso.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//IF THEIR IS CONSTRUCTOR PARAMETER IN VIEWMODEL USE VIEWMODEL FACTORY
class TransViewModelFactory(val repo:TransactionRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TransactionViewModel::class.java)){
            return TransactionViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}