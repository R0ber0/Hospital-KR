package com.kotov.hospital

import android.Manifest
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotov.hospital.data.database.HospitalDatabase
import com.kotov.hospital.data.entities.Patient
import com.kotov.hospital.data.repository.HospitalRepository
import com.kotov.hospital.databinding.ActivityMainBinding
import com.kotov.hospital.ui.PatientAdapter
import com.kotov.hospital.utils.CalendarHelper
import com.kotov.hospital.viewmodel.HospitalViewModel
import com.kotov.hospital.viewmodel.HospitalViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val patientAdapter = PatientAdapter()

    private val viewModel: HospitalViewModel by lazy {
        val database = HospitalDatabase.getDatabase(this)
        val repository = HospitalRepository(database)
        val factory = HospitalViewModelFactory(repository)
        ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private val calendarPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Разрешение на календарь получено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Разрешение на календарь отклонено", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSpinnersAndButtons()
        observeData()

        // Обработка кнопки "Записать в календарь"
        patientAdapter.setOnCalendarClickListener { patient ->
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {

                calendarPermissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
            } else {
                CalendarHelper.addEventToCalendar(
                    context = this,
                    title = "Приём пациента: ${patient.fullName}",
                    description = "Пациент: ${patient.fullName}\nДиагноз ID: ${patient.diagnosisId ?: "Не указан"}"
                )
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvResults.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = patientAdapter
        }
    }

    private fun setupSpinnersAndButtons() {
        // Spinner для отделений
        val deptAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf<String>())
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDepartment.adapter = deptAdapter

        // Spinner для диагнозов
        val diagAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf<String>())
        diagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDiagnosis.adapter = diagAdapter

        // Кнопка "Показать врачей"
        binding.btnShowDoctors.setOnClickListener {
            val pos = binding.spinnerDepartment.selectedItemPosition
            if (pos >= 0 && viewModel.departments.value.isNotEmpty()) {
                val dept = viewModel.departments.value[pos]
                viewModel.loadDoctorsByDepartment(dept.id)
                Toast.makeText(this, "Загрузка врачей из ${dept.name}...", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка "Показать пациентов"
        binding.btnShowPatientsByDiagnosis.setOnClickListener {
            val pos = binding.spinnerDiagnosis.selectedItemPosition
            if (pos >= 0 && viewModel.diagnoses.value.isNotEmpty()) {
                val diagnosis = viewModel.diagnoses.value[pos]
                viewModel.loadPatientsByDiagnosis(diagnosis.id)
                Toast.makeText(this, "Загрузка пациентов с диагнозом ${diagnosis.code}...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.departments.collect { departments ->
                    val names = departments.map { it.name }
                    (binding.spinnerDepartment.adapter as ArrayAdapter<String>).apply {
                        clear()
                        addAll(names)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.diagnoses.collect { diagnoses ->
                    val names = diagnoses.map { "${it.code} - ${it.description}" }
                    (binding.spinnerDiagnosis.adapter as ArrayAdapter<String>).apply {
                        clear()
                        addAll(names)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.patientsByDiagnosis.collect { patients ->
                    patientAdapter.submitList(patients)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doctors.collect { doctors ->
                    if (doctors.isNotEmpty()) {
                        val items = doctors.map { doctor ->
                            Patient(
                                id = doctor.id,
                                fullName = doctor.fullName + " (врач)",
                                doctorId = doctor.id,
                                diagnosisId = null
                            )
                        }
                        patientAdapter.submitList(items)
                    }
                }
            }
        }
    }
}