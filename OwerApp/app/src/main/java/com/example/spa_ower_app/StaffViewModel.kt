package com.example.spa_ower_app

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
    val state: Boolean = false
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
}