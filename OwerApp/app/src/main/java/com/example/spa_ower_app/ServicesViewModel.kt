package com.example.spa_ower_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Service(
    val id: String = "",
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
    init {
        fetchServices()
    }

    private fun fetchServices() {
        db.collection("Services")
            .get()
            .addOnSuccessListener { result ->
                val newServices = mutableListOf<Service>()
                val newIds = mutableListOf<String>()

                for (doc in result) {
                    val service = doc.toObject(Service::class.java).copy(id = doc.id)
                    newServices.add(service)
                    newIds.add(doc.id)
                }

                services = newServices
            }
            .addOnFailureListener {
                    exception ->
                Log.e("ServiceViewModel", "Error getting services", exception)
            }
    }
}
