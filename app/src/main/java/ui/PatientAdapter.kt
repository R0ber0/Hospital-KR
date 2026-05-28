package com.kotov.hospital.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kotov.hospital.data.entities.Patient
import com.kotov.hospital.databinding.ItemPatientBinding

class PatientAdapter : ListAdapter<Patient, PatientAdapter.PatientViewHolder>(PatientDiffCallback()) {

    private var onCalendarClick: ((Patient) -> Unit)? = null

    fun setOnCalendarClickListener(listener: (Patient) -> Unit) {
        onCalendarClick = listener
    }

    class PatientViewHolder(private val binding: ItemPatientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(patient: Patient, onCalendarClick: ((Patient) -> Unit)?) {
            binding.tvFullName.text = patient.fullName
            binding.tvDiagnosis.text = "Диагноз ID: ${patient.diagnosisId ?: "Не указан"}"

            binding.btnAddToCalendar.setOnClickListener {
                onCalendarClick?.invoke(patient)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(getItem(position), onCalendarClick)
    }

    class PatientDiffCallback : DiffUtil.ItemCallback<Patient>() {
        override fun areItemsTheSame(oldItem: Patient, newItem: Patient): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Patient, newItem: Patient): Boolean =
            oldItem == newItem
    }
}