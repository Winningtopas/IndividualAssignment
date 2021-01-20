package com.example.shoppinglistkotlin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [GameHistoryStats::class], version = 1, exportSchema = false)
    @TypeConverters(Converters::class)
    abstract class ShoppingListRoomDatabase : RoomDatabase() {

        abstract fun productDao(): GameHistoryDao

    companion object {
            private const val DATABASE_NAME = "SHOPPING_LIST_DATABASE"

            @Volatile
            private var shoppingListRoomDatabaseInstance: ShoppingListRoomDatabase? = null

            fun getDatabase(context: Context): ShoppingListRoomDatabase? {
                if (shoppingListRoomDatabaseInstance == null) {
                    synchronized(ShoppingListRoomDatabase::class.java) {
                        if (shoppingListRoomDatabaseInstance == null) {
                            shoppingListRoomDatabaseInstance =
                                Room.databaseBuilder(context.applicationContext,ShoppingListRoomDatabase::class.java, DATABASE_NAME).build()
                        }
                    }
                }
                return shoppingListRoomDatabaseInstance
            }
        }

    }
