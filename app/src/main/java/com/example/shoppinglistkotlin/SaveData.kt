package com.example.shoppinglistkotlin

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "save_data_table")
data class SaveData(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "currency")
    var currency: Int,

    @ColumnInfo(name = "unlockedLevel")
    var unlockedLevel: Int

) : Parcelable