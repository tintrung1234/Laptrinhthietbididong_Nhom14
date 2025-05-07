package com.example.spa_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Staff(
    val id: String = "",
    val image: String = "",
    val name: String = "",
    val rating: Int = 0,
    val rateCount: Int = 0,
)

class StaffViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var staffs by mutableStateOf<List<Staff>>(emptyList())
        private set
    init {
        fetchStaffs()
    }

    private fun fetchStaffs() {
        db.collection("Staff")
            .get()
            .addOnSuccessListener { result ->
                val newStaffs = mutableListOf<Staff>()
                val newIds = mutableListOf<String>()

                for (doc in result) {
                    val staff = doc.toObject(Staff::class.java).copy(id = doc.id)
                    newStaffs.add(staff)
                    newIds.add(doc.id)
                }

                staffs = newStaffs
            }
            .addOnFailureListener {
                    exception ->
                Log.e("StaffViewModel", "Error getting staffs", exception)
            }
    }

    fun updateRateFirestore(staff: Staff, rate: Int) {
        val db = FirebaseFirestore.getInstance()
        val newRating = calculateAverageRating(staff.rating, rate, staff.rateCount)
        val newRateCount = staff.rateCount + 1
        val updates = mapOf(
            "rating" to newRating,
            "rateCount" to newRateCount,
        )
        db.collection("Staff")
            .document(staff.id)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Staff rate updated to 1")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating rate", e)
            }
    }
}