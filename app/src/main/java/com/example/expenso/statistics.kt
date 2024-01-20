package com.example.expenso

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expenso.model.TransactionDatabase
import com.example.expenso.mvvm.TransViewModelFactory
import com.example.expenso.mvvm.TransactionRepository
import com.example.expenso.mvvm.TransactionViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class statistics : AppCompatActivity() {
    private lateinit var viewModel: TransactionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val dao = TransactionDatabase.invoke(this).transactionDao()
        val repo = TransactionRepository(dao)
        val factory = TransViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)

        viewModel.getAll.observe(this@statistics, Observer {

            val income = it.filter { it.isIncome == true }.map { it.amount }.sum()
            val expense = it.filter { it.isIncome == false }.map { it.amount }.sum()
            val balance = income - expense

            val pieChart = findViewById<PieChart>(R.id.pieChart)


            //initializing colors for the entries


            val colors = ArrayList<Int>()
            colors.add(Color.parseColor("#304567"))
            colors.add(Color.parseColor("#309967"))
            colors.add(Color.parseColor("#476567"))
            colors.add(Color.parseColor("#890567"))
            colors.add(Color.parseColor("#a35567"))
            colors.add(Color.parseColor("#ff5f67"))
            colors.add(Color.parseColor("#3ca567"))

            val entries = listOf<PieEntry>(
                PieEntry(income.toFloat(), "Income"),
                PieEntry(expense.toFloat(), "Expense"),
                PieEntry(balance.toFloat(), "balance")

            )

            val pieDataSet = PieDataSet(entries, "Pie Chart")
            pieDataSet.valueTextSize = 12f
            //providing color list for coloring different entries
            //providing color list for coloring different entries
            pieDataSet.colors = colors
            //grouping the data set from entry to chart
            //grouping the data set from entry to chart
            val pieData = PieData(pieDataSet)
            //showing the value of the entries, default true if not set
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true)
            pieChart.animateY(1400, Easing.EaseInOutQuad)

            pieChart.setData(pieData)
            pieChart.invalidate()

            val lineChart = findViewById<LineChart>(R.id.lineChart)


            val incomes = it.filter { it.isIncome == true }.map { it.amount }
            val expenses = it.filter{it.isIncome == false}.map { it.amount }
            val entries2 = arrayListOf<Entry>()
            entries2.add(Entry())

            for(i in incomes){
                for(j in expenses)
                entries2.add(Entry(i.toFloat(),j.toFloat()))
            }
            val vl = LineDataSet(entries, "My Type")

//Part4
            vl.setDrawValues(false)
            vl.setDrawFilled(true)
            vl.lineWidth = 3f
            vl.fillColor = R.color.darkGray
            vl.fillAlpha = R.color.red

//Part5
            lineChart.xAxis.labelRotationAngle = 0f

//Part6
            lineChart.data = LineData(vl)

//Part7
            lineChart.axisRight.isEnabled = false
            lineChart.xAxis.axisMaximum = +0.1f

//Part8
            lineChart.setTouchEnabled(true)
            lineChart.setPinchZoom(true)

//Part9
            lineChart.description.text = "Income vs Expense"
            lineChart.setNoDataText("No forex yet!")

//Part10
            lineChart.animateX(1800, Easing.EaseInExpo)

//Part11

        })


    }
}