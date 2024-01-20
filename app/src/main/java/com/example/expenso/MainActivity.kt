package com.example.expenso

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.databinding.ActivityMainBinding
import com.example.expenso.model.Transaction
import com.example.expenso.model.TransactionDao
import com.example.expenso.model.TransactionDatabase
import com.example.expenso.mvvm.TransViewModelFactory
import com.example.expenso.mvvm.TransactionRepository
import com.example.expenso.mvvm.TransactionViewModel
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlin.math.absoluteValue


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var transactions = listOf<Transaction>()
    private lateinit var viewModel: TransactionViewModel
    private lateinit var transactionAdapter: TransactionAdapter
    private var income: Double = 0.0
    private var expense: Double = 0.0
    private var balance: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)






        transactionAdapter = TransactionAdapter(this@MainActivity, transactions)

        binding.addTrnsaction.setOnClickListener {
            val intent = Intent(this@MainActivity, add_transaction::class.java)
            startActivity(intent)
        }
        val dao = TransactionDatabase.invoke(this).transactionDao()
        val repo = TransactionRepository(dao)
        val factory = TransViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

            viewModel.getAll.observe(this@MainActivity, Observer {
                adapter = TransactionAdapter(this@MainActivity, it)


            })



        }

        viewModel.getAll.observe(this@MainActivity, Observer {

            income = it.filter { it.isIncome == true }.map { it.amount }.sum()
            expense = it.filter { it.isIncome == false }.map { it.amount }.sum()
            balance = income - expense

            binding.tvbudget.text = income.toString()
            binding.tvexpense.text = expense.toString()
            binding.tvbalance.text = balance.absoluteValue.toString()

            transactions = it


    })

        ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val transaction = transactions[position]
                viewModel.deleteTransaction(transaction)
                //transactionAdapter.notifyDataSetChanged()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {


                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete)
                    .create()
                    .decorate();
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }



        }).attachToRecyclerView(binding.recyclerView)

        lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is TransactionViewModel.TransactionEvent.ShowUndoDeleteMessage -> {
                        Snackbar.make(
                            findViewById(R.id.coordinator),
                            "Transaction Deleted",
                            Snackbar.LENGTH_SHORT
                        ).setAction("UNDO") {
                            viewModel.onClickUndoDelete(event.transaction)

                        }.show()
                    }
                }
            }
        }
    navigateToStatistics()
}




    fun navigateToStatistics() {
        binding.statistics.setOnClickListener {

            val intent = Intent(this@MainActivity, statistics::class.java)

            startActivity(intent)

        }
    }

}
