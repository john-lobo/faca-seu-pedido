package com.jlndev.productservice.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OrderHistoryTable")
class OrderHistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val order: String,
    val quantityItems: Int,
    val totalValue: Double
)