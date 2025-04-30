package com.example.spa_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Appointment(
    val id: String = "",
    val userId: String = "",
    val staffId: Any = 0,
    val servicesId: Int = 0,
    val orderDate: String = "",
    val pickedDate: String = "",
    val state: Int = 0,
    val totalValues: Float = 0f,
    val paymentMethod: Boolean = false,
)
class AppointmentViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var appointments by mutableStateOf<List<Appointment>>(emptyList())
        private set
    var appointmentsID by mutableStateOf<List<String>>(emptyList())
        private set
    init {
        fetchAppointments()
    }
    private fun fetchAppointments() {
        db.collection("Appointments")
            .get()
            .addOnSuccessListener { result ->
                val newAppointments = mutableListOf<Appointment>()
                val newIds = mutableListOf<String>()

                for (doc in result) {
                    val appointment = doc.toObject(Appointment::class.java).copy(id = doc.id)
                    newAppointments.add(appointment)
                    newIds.add(doc.id)
                }

                appointments = newAppointments
                appointmentsID = newIds
            }
            .addOnFailureListener { exception ->
                Log.e("AppointmentViewModel", "Error getting appointments", exception)
            }
    }

}