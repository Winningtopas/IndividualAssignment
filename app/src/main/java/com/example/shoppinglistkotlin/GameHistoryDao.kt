package com.example.shoppinglistkotlin

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

    @Dao
    interface GameHistoryDao {

        @Query("SELECT * FROM product_table")
        suspend fun getAllProducts(): List<GameHistoryStats>

        @Insert
        suspend fun insertProduct(gameHistoryStats: GameHistoryStats)

        @Delete
        suspend fun deleteProduct(gameHistoryStats: GameHistoryStats)

        @Query("DELETE FROM product_table")
        suspend fun deleteAllProducts()

    }

