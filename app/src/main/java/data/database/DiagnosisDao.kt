package com.kotov.hospital.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kotov.hospital.data.entities.Diagnosis
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {
    @Query("SELECT * FROM diagnoses")
    fun getAllDiagnoses(): Flow<List<Diagnosis>>

    @Insert
    suspend fun insert(diagnosis: Diagnosis)
}