package com.example.spa_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Appointment(
    val UserId: String = "",
    val StaffId: Int = 0,
    val ServicesId: Int = 0,
    val OrderDate: String = "",
    val PickedDate: String = "",
    val State: Int = 0,
    val TotalValues: Int = 0,
    val PaymentMethod: Boolean = false,
)
class AppointmentViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var appointments by mutableStateOf<List<Appointment>>(emptyList())
        private set
    var appointmentsID by mutableStateOf<List<Int>>(emptyList())
        private set
    init {
        fetchAppointments()
    }
    private fun fetchAppointments() {
        db.collection("Appointments")
            .get()
            .addOnSuccessListener { result ->
                appointmentsID = result.mapNotNull { it.id.toIntOrNull() }
                appointments = result.mapNotNull { it.toObject(Appointment::class.java) }
            }
            .addOnFailureListener {
                    exception ->
                Log.e("AppointmentViewModel", "Error getting appointments", exception)
            }
    }
}