package com.example.groceryapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), GroceryRVAdapter.groceryItemClickInterface{
    lateinit var itemsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var list:List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var groceryVeiwModal: GroceryVeiwModal


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemsRV = findViewById(R.id.idRVItems)
        addFAB= findViewById(R.id.idFABAdd)
        list = ArrayList<GroceryItems>()
        groceryRVAdapter = GroceryRVAdapter(list, this)
        itemsRV.layoutManager= LinearLayoutManager(this)
        itemsRV.adapter=groceryRVAdapter
        val groceryRepository= GroceryRepository(GroceryDatabase(this))
        val factory= GroceryViewModalFactory(groceryRepository)
        groceryVeiwModal= ViewModelProvider(this,factory).get(GroceryVeiwModal::class.java)
        groceryVeiwModal.getAllGroceryItems().observe(this, Observer {
            groceryRVAdapter.list =it
            groceryRVAdapter.notifyDataSetChanged()
        })

        addFAB.setOnClickListener{
            openDialog()
        }

    }

    fun openDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grocery_add_dailog)
        val cancelBtn = dialog.findViewById<Button>(R.id.idBtncancel)
        val addBtn = dialog.findViewById<Button>(R.id.idBtnAdd)
        val itemEdt = dialog.findViewById<EditText>(R.id.idEdtItemName)
        val itemPriceEdt = dialog.findViewById<EditText>(R.id.idEdtItemPrice)
        val itemQuantityEdt = dialog.findViewById<EditText>(R.id.idEdtItemQuantity)
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        addBtn.setOnClickListener {
            val itenNane: String = itemEdt.text.toString()
            val itemPrice: String = itemPriceEdt.text.toString()
            val itenQuantity: String = itemQuantityEdt.text.toString()
            val qty: Int = itenQuantity.toInt()
            val pr: Int = itemPrice.toInt()
            if (itenNane.isNotEmpty() && itemPrice.isNotEmpty() && itenQuantity.isNotEmpty()) {
                val itens: GroceryItems (itemNane, qty, pr)
                groceryVeiwModal.insert(itens)
                Toast.makeText(applicationContext, "Item Inserted..", Toast.LENGTH_SHORT).show()
                groceryRVAdapter.notifyDataSetChanged()
                dialog.dismiss()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please enter all the data..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        dialog.show()
    }
    override fun onItemClick(groceryItems: GroceryItems) {
        groceryVeiwModal.delete(groceryItems)
        groceryRVAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext,"Item Deleted..", Toast.LENGTH_SHORT).show()

    }
}