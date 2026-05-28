package com.kotov.hospital.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kotov.hospital.data.entities.Doctor
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {
    @Query("SELECT * FROM doctors WHERE departmentId = :departmentId")
    fun getDoctorsByDepartment(departmentId: Long): Flow<List<Doctor>>

    @Insert
    suspend fun insert(doctor: Doctor)
}