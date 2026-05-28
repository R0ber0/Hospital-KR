package com.kotov.hospital.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnoses")
data class Diagnosis(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val description: String
)