package com.example.opsc6311app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private var expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.itemTitle)
        val amount: TextView = itemView.findViewById(R.id.itemAmount)
        val date: TextView = itemView.findViewById(R.id.itemDate)
        val description: TextView = itemView.findViewById(R.id.itemDescription)
        val imageUri: TextView = itemView.findViewById(R.id.itemImageUri)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.title.text = "Title: ${expense.title}"
        holder.amount.text = "Amount: ${expense.amount}"
        holder.date.text = "Date: ${expense.date}"
        holder.description.text = "Description: ${expense.description}"
        holder.imageUri.text = "Image URL: ${expense.imageUri}"
    }

    override fun getItemCount(): Int = expenses.size

    fun updateData(newData: List<Expense>) {
        expenses = newData
        notifyDataSetChanged()
    }
}
