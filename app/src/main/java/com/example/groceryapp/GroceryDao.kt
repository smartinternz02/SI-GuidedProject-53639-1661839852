package com.example.groceryapp

import androidx
import androidx.lifecycle.LiveData

..Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
@Dao
class GroceryDao {
    @insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insert(item: GroceryItems)

    @Delete
    suspend fun delete(item: GroceryItems)

    @Query("SELECT * FROM grocery_items")
    fun getAllGroceryItems() : LiveData<List<GroceryItems>>

}