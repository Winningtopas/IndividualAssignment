package com.example.shoppinglistkotlin

import android.content.Context


class GameHistoryRepository(context: Context) {

    private val gameHistoryDao: GameHistoryDao

    init {

        val database = ShoppingListRoomDatabase.getDatabase(context)
        gameHistoryDao = database!!.productDao()
    }

    suspend fun getAllProducts(): List<GameHistoryStats> = gameHistoryDao.getAllProducts()

    suspend fun insertProduct(gameHistoryStats: GameHistoryStats) = gameHistoryDao.insertProduct(gameHistoryStats)

    suspend fun deleteProduct(gameHistoryStats: GameHistoryStats) = gameHistoryDao.deleteProduct(gameHistoryStats)

    suspend fun deleteAllProducts() = gameHistoryDao.deleteAllProducts()
}
