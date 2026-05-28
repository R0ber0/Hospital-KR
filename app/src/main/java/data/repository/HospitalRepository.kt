package com.kotov.hospital.data.repository

import com.kotov.hospital.data.database.HospitalDatabase
import com.kotov.hospital.data.entities.Department
import com.kotov.hospital.data.entities.Doctor
import com.kotov.hospital.data.entities.Diagnosis
import com.kotov.hospital.data.entities.Patient
import kotlinx.coroutines.flow.Flow

class HospitalRepository(private val database: HospitalDatabase) {

    private val departmentDao = database.departmentDao()
    private val doctorDao = database.doctorDao()
    private val diagnosisDao = database.diagnosisDao()
    private val patientDao = database.patientDao()

    // Departments
    fun getAllDepartments(): Flow<List<Department>> = departmentDao.getAllDepartments()
    suspend fun insertDepartment(department: Department) = departmentDao.insert(department)

    // Doctors
    fun getDoctorsByDepartment(departmentId: Long): Flow<List<Doctor>> =
        doctorDao.getDoctorsByDepartment(departmentId)
    suspend fun insertDoctor(doctor: Doctor) = doctorDao.insert(doctor)

    // Diagnoses
    fun getAllDiagnoses(): Flow<List<Diagnosis>> = diagnosisDao.getAllDiagnoses()
    suspend fun insertDiagnosis(diagnosis: Diagnosis) = diagnosisDao.insert(diagnosis)

    // Patients
    fun getPatientsByDoctor(doctorId: Long): Flow<List<Patient>> =
        patientDao.getPatientsByDoctor(doctorId)
    fun getPatientsByDiagnosis(diagnosisId: Long): Flow<List<Patient>> =
        patientDao.getPatientsByDiagnosis(diagnosisId)
    suspend fun insertPatient(patient: Patient) = patientDao.insert(patient)
}