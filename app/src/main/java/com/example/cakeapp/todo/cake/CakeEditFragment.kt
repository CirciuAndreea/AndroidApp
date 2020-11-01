package com.example.cakeapp.todo.cake

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.cakeapp.core.TAG
import com.example.cakeapp.R
import kotlinx.android.synthetic.main.fragment_cake_edit.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CakeEditFragment : Fragment() {
    companion object {
        const val ITEM_ID = "_ID"
        const val COUNTERTOPS="countertops"
        const val CREAM="cream"
        const val AMOUNT="amount"
        const val DESIGN="design"
        const val USER_ID="user_id"
    }

    private lateinit var viewModel: CakeEditViewModel
    private var itemId: String? = null
    private var countertops: String? = null
    private var cream: String? = null
    private var amount: String? = null
    private var design: Boolean = false
    private var userId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                itemId = it.getString(ITEM_ID).toString()
            }
            if (it.containsKey(COUNTERTOPS))
            {
                countertops=it.getString(COUNTERTOPS);
            }
            if (it.containsKey(CREAM))
            {
                cream=it.getString(CREAM);
            }
            if (it.containsKey(AMOUNT))
            {
                amount=it.getString(AMOUNT).toString();
            }
            if (it.containsKey(DESIGN))
            {
                design=it.getBoolean(DESIGN);
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_cake_edit, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated")
        cake_name.setText(itemId)
        cake_countertops.setText(countertops);
        cake_cream.setText(cream);
        cake_amount.setText(amount);
        cake_design.isChecked = design;



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save item")
            viewModel.saveOrUpdateItem(
                cake_name.text.toString(),
                cake_countertops.text.toString(),
                cake_cream.text.toString(),
                cake_amount.text.toString().toInt(),
                cake_design.isChecked
            )
        }
        button_delete.setOnClickListener{
            viewModel.deleteItem()
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(CakeEditViewModel::class.java)
        viewModel.item.observe(viewLifecycleOwner) { item ->
            Log.v(TAG, "update items")
            cake_name.setText(item.name)
        }
        viewModel.fetching.observe(viewLifecycleOwner) { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        }
        viewModel.fetchingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = itemId
        if (id != null) {
            viewModel.loadItem(id)
        }
    }
}
