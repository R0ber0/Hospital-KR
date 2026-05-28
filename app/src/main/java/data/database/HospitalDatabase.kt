package com.kotov.hospital.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kotov.hospital.data.entities.Department
import com.kotov.hospital.data.entities.Doctor
import com.kotov.hospital.data.entities.Diagnosis
import com.kotov.hospital.data.entities.Patient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Department::class, Doctor::class, Diagnosis::class, Patient::class],
    version = 1,
    exportSchema = false
)
abstract class HospitalDatabase : RoomDatabase() {

    abstract fun departmentDao(): DepartmentDao
    abstract fun doctorDao(): DoctorDao
    abstract fun diagnosisDao(): DiagnosisDao
    abstract fun patientDao(): PatientDao

    companion object {
        @Volatile
        private var INSTANCE: HospitalDatabase? = null

        fun getDatabase(context: Context): HospitalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HospitalDatabase::class.java,
                    "hospital_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    fillInitialData(database)
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private suspend fun fillInitialData(database: HospitalDatabase) {
            val deptDao = database.departmentDao()
            val docDao = database.doctorDao()
            val diagDao = database.diagnosisDao()
            val patientDao = database.patientDao()

            // Отделения
            val cardio = Department(name = "Кардиология")
            val therapy = Department(name = "Терапия")
            val surgery = Department(name = "Хирургия")
            deptDao.insert(cardio)
            deptDao.insert(therapy)
            deptDao.insert(surgery)

            // Диагнозы
            diagDao.insert(Diagnosis(code = "I20", description = "Стенокардия"))
            diagDao.insert(Diagnosis(code = "J45", description = "Бронхиальная астма"))
            diagDao.insert(Diagnosis(code = "K25", description = "Язва желудка"))
            diagDao.insert(Diagnosis(code = "M54", description = "Остеохондроз"))

            // Врачи
            docDao.insert(Doctor(fullName = "Иванов И.И.", isHead = true, departmentId = 1))
            docDao.insert(Doctor(fullName = "Петрова А.С.", isHead = false, departmentId = 1))
            docDao.insert(Doctor(fullName = "Сидоров В.П.", isHead = true, departmentId = 2))

            // Пациенты
            patientDao.insert(Patient(fullName = "Смирнов А.А.", doctorId = 1, diagnosisId = 1))
            patientDao.insert(Patient(fullName = "Кузнецова Е.В.", doctorId = 1, diagnosisId = 2))
            patientDao.insert(Patient(fullName = "Попов Д.С.", doctorId = 2, diagnosisId = 3))
            patientDao.insert(Patient(fullName = "Соколова М.И.", doctorId = 3, diagnosisId = 4))
        }
    }
}