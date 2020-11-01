package com.example.cakeapp.todo.cakes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cakeapp.R
import kotlinx.android.synthetic.main.view_cake.view.*
import com.example.cakeapp.core.TAG
import com.example.cakeapp.todo.data.Cake
import com.example.cakeapp.todo.cake.CakeEditFragment

class CakeListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<CakeListAdapter.ViewHolder>() {

    var items = emptyList<Cake>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener;

    init {
        onItemClick = View.OnClickListener { view ->
            val item = view.tag as Cake
            fragment.findNavController().navigate(R.id.fragment_cake_edit, Bundle().apply {
                putString(CakeEditFragment.ITEM_ID, item._id)
                putString(CakeEditFragment.COUNTERTOPS,item.countertops)
                putString(CakeEditFragment.CREAM,item.cream)
                putString(CakeEditFragment.AMOUNT,item.amount.toString())
                putBoolean(CakeEditFragment.DESIGN,item.design)
                putString(CakeEditFragment.USER_ID,item.userId)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_cake, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val item = items[position]
        holder.itemView.tag = item
        holder.textView.text = item.name
        holder.itemView.setOnClickListener(onItemClick)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text
    }
}
