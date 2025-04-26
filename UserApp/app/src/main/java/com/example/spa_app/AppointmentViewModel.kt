package com.example.spa_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Appointment(
    val UserId: String,
    val StaffId: Int,
    val ServicesId: Int,
    val OrderDate: String,
    val PickedDate: String,
    val State: Boolean,
    val TotalValues: Int,
    val PaymentMethod: String,
)
class AppointmentViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var appointments by mutableStateOf<List<Appointment>>(emptyList())
        private set
    init {
        fetchAppointments()
    }
    private fun fetchAppointments() {
        db.collection("Appointments")
            .get()
            .addOnSuccessListener { result ->
                appointments = result.mapNotNull { it.toObject(Appointment::class.java) }
            }
            .addOnFailureListener {
                    exception ->
                Log.e("AppointmentViewModel", "Error getting appointments", exception)
            }
    }
}