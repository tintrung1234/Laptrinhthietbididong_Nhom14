package com.example.spa_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Service(
    val id: String = "",
    val categoryId: Int = 0,
    val description: String = "",
    val discount: Int = 0,
    val image: String = "",
    val name: String = "",
    val overalTime: Int = 0,
    val price: Float = 0f,
    val rating: Int = 0,
    val rateCount: Int = 0,
    val visitors: Int = 0
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

    fun updateRateFirestore(service: Service, rate: Int) {
        val db = FirebaseFirestore.getInstance()
        val newRating = calculateAverageRating(service.rating, rate, service.rateCount)
        val newRateCount = service.rateCount + 1
        val updates = mapOf(
            "rating" to newRating,
            "rateCount" to newRateCount,
        )
        db.collection("Services")
            .document(service.id)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Services rate updated to 1")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating rate", e)
            }
    }
}
