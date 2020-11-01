package com.example.cakeapp.todo.cakes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.cakeapp.R
import kotlinx.android.synthetic.main.fragment_cake_list.*
import com.example.cakeapp.auth.data.AuthRepository
import com.example.cakeapp.core.TAG

class CakeListFragment : Fragment() {
    private lateinit var itemListAdapter: CakeListAdapter
    private lateinit var itemsModel: CakeListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cake_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        if (!AuthRepository.isLoggedIn) {
            findNavController().navigate(R.id.fragment_login)
            return;
        }
        setupItemList()
        fab.setOnClickListener {
            Log.v(TAG, "add new item")
            findNavController().navigate(R.id.fragment_cake_edit)
        }
    }

    private fun setupItemList() {
        itemListAdapter = CakeListAdapter(this)
        item_list.adapter = itemListAdapter
        itemsModel = ViewModelProvider(this).get(CakeListViewModel::class.java)
        itemsModel.items.observe(viewLifecycleOwner) { items ->
            Log.v(TAG, "update items")
            itemListAdapter.items = items
        }
        itemsModel.loading.observe(viewLifecycleOwner) { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        }
        itemsModel.loadingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
        itemsModel.loadItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }
}