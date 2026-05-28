package com.kotov.hospital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotov.hospital.data.entities.Department
import com.kotov.hospital.data.entities.Diagnosis
import com.kotov.hospital.data.entities.Doctor
import com.kotov.hospital.data.entities.Patient
import com.kotov.hospital.data.repository.HospitalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HospitalViewModel(private val repository: HospitalRepository) : ViewModel() {

    private val _departments = MutableStateFlow<List<Department>>(emptyList())
    val departments: StateFlow<List<Department>> = _departments.asStateFlow()

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    private val _patientsByDiagnosis = MutableStateFlow<List<Patient>>(emptyList())
    val patientsByDiagnosis: StateFlow<List<Patient>> = _patientsByDiagnosis.asStateFlow()

    private val _diagnoses = MutableStateFlow<List<Diagnosis>>(emptyList())
    val diagnoses: StateFlow<List<Diagnosis>> = _diagnoses.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.getAllDepartments().collect { _departments.value = it }
        }
        viewModelScope.launch {
            repository.getAllDiagnoses().collect { _diagnoses.value = it }
        }
    }

    fun loadDoctorsByDepartment(departmentId: Long) {
        viewModelScope.launch {
            repository.getDoctorsByDepartment(departmentId).collect {
                _doctors.value = it
            }
        }
    }

    fun loadPatientsByDiagnosis(diagnosisId: Long) {
        viewModelScope.launch {
            repository.getPatientsByDiagnosis(diagnosisId).collect {
                _patientsByDiagnosis.value = it
            }
        }
    }
}