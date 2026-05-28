package com.kotov.hospital.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "patients",
    foreignKeys = [
        ForeignKey(
            entity = Doctor::class,
            parentColumns = ["id"],
            childColumns = ["doctorId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Diagnosis::class,
            parentColumns = ["id"],
            childColumns = ["diagnosisId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val doctorId: Long? = null,
    val diagnosisId: Long? = null
)