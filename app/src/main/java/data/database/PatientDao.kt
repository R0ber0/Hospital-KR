package com.kotov.hospital.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kotov.hospital.data.entities.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients WHERE doctorId = :doctorId")
    fun getPatientsByDoctor(doctorId: Long): Flow<List<Patient>>

    @Query("SELECT * FROM patients WHERE diagnosisId = :diagnosisId")
    fun getPatientsByDiagnosis(diagnosisId: Long): Flow<List<Patient>>

    @Insert
    suspend fun insert(patient: Patient)
}