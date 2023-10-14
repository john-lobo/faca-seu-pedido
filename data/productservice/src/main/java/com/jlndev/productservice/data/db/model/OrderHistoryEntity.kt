package com.jlndev.productservice.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OrderEntity")
class OrderHistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1,
    val order: String
)