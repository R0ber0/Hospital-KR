package com.kotov.hospital.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kotov.hospital.data.entities.Department
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentDao {
    @Query("SELECT * FROM departments")
    fun getAllDepartments(): Flow<List<Department>>

    @Insert
    suspend fun insert(department: Department)
}