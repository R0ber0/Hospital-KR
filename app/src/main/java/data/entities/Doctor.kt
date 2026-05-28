package com.kotov.hospital.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "doctors",
    foreignKeys = [
        ForeignKey(
            entity = Department::class,
            parentColumns = ["id"],
            childColumns = ["departmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Doctor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val isHead: Boolean = false,
    val departmentId: Long
)