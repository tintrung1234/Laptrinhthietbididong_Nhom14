package com.example.spa_ower_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

data class Appointment(
    val id: String = "",
    val userId: String = "",

    //use if client want book with new info
    val userName: String = "",
    val phone: String = "",
    val email: String = "",
    //

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
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    val updatedAppointments = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Appointment::class.java)?.copy(id = doc.id)
                    }.sortedByDescending { appointment ->
                        try {
                            dateFormat.parse(appointment.orderDate)
                        } catch (e: Exception) {
                            null
                        }
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
            .update(
                mapOf(
                    "state" to 1,
                    "paymentMethod" to true
                )
            )
            .addOnSuccessListener {
                Log.d("Firestore", "Appointment updated: state = 1, paymentMethod = true")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating appointment", e)
            }
    }



}