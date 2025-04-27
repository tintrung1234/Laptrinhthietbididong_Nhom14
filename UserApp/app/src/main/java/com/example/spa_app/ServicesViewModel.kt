package com.example.spa_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Service(
    val CategoryId: Int = 0,
    val Description: String = "",
    val Discount: Int = 0,
    val Image: String = "",
    val Name: String = "",
    val OveralTime: Int = 0,
    val Price: Float = 0f,
    val Rating: Int = 0,
    val Visitors: Int = 0
)

class ServiceViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var services by mutableStateOf<List<Service>>(emptyList())
        private set
    var servicesID by mutableStateOf<List<Int>>(emptyList())
        private set

    init {
        fetchServices()
    }

    private fun fetchServices() {
        db.collection("Services")
            .get()
            .addOnSuccessListener { result ->
                servicesID = result.mapNotNull { it.id.toIntOrNull() }
                services = result.mapNotNull { it.toObject(Service::class.java) }
            }
            .addOnFailureListener {
                    exception ->
                Log.e("ServiceViewModel", "Error getting services", exception)
            }
    }
}
