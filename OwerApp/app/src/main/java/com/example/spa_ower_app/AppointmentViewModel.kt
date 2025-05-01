package com.example.spa_ower_app

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
    val servicesId: String = "",
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
    init {
        fetchAppointments()
    }

    private fun fetchAppointments() {
        db.collection("Appointments")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("AppointmentViewModel", "Real-time fetch failed", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val updatedAppointments = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Appointment::class.java)?.copy(id = doc.id)
                    }
                    appointments = updatedAppointments
                } else {
                    appointments = emptyList()
                }
            }
    }

    fun updateAppointmentInFirestore(appointment: Appointment) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Appointments")
            .document(appointment.id)
            .update("state", 1)
            .addOnSuccessListener {
                Log.d("Firestore", "Appointment state updated to 1")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating state", e)
            }
    }


}