package com.jlndev.productservice.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OrderHistoryTable")
class OrderHistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1,
    val order: String,
    val totalValue: Double
)