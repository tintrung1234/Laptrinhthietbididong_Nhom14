package com.example.spa_ower_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
            .addOnFailureListener { exception ->
                Log.e("ServiceViewModel", "Error getting services", exception)
            }
    }

    fun saveService(
        id: String,
        categoryId: Int,
        description: String,
        discount: Int,
        image: String,
        name: String,
        overalTime: Int,
        price: Float,
        rating: Int,
        visitors: Int,
        onSuccess: () -> Unit = {}, // Callback
        onFailure: (Exception) -> Unit = {}
    ) {
        // Validate required fields
        if (name.isBlank() || description.isBlank() || image.isBlank()) {
            Log.e("SaveService", "Name, Description, and Image must not be empty.")
            return
        }

        if (price <= 0f) {
            Log.e("SaveService", "Price must be greater than 0.")
            return
        }

        if (overalTime <= 0) {
            Log.e("SaveService", "OveralTime must be greater than 0.")
            return
        }

        if (categoryId <= 0) {
            Log.e("SaveService", "CategoryId must be valid (> 0).")
            return
        }

        val docId = if (id.isBlank()) db.collection("Services").document().id else id
        val docRef = db.collection("Services").document(docId)

        val newService = Service(
            id = docId,
            categoryId = categoryId,
            description = description,
            discount = discount,
            image = image,
            name = name,
            overalTime = overalTime,
            price = price,
            rating = rating,
            visitors = visitors
        )

        docRef.set(newService)
            .addOnSuccessListener {
                fetchServices() // refresh after saving
                Log.d("SaveService", "Success")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("SaveService", "Error: ", exception)
                onFailure(exception)
            }
    }

    fun updateService(
        id: String,
        categoryId: Int,
        description: String,
        discount: Int,
        image: String,
        name: String,
        overalTime: Int,
        price: Float,
        rating: Int,
        visitors: Int,
        onSuccess: () -> Unit = {}, // Callback
        onFailure: (Exception) -> Unit = {}
    ) {
        if (id.isBlank()) {
            Log.e("UpdateService", "ID is required to update a service.")
            return
        }

        if (name.isBlank() || description.isBlank() || image.isBlank()) {
            Log.e("SaveService", "Name, Description, and Image must not be empty.")
            return
        }

        if (price <= 0f) {
            Log.e("SaveService", "Price must be greater than 0.")
            return
        }

        if (overalTime <= 0) {
            Log.e("SaveService", "OveralTime must be greater than 0.")
            return
        }

        if (categoryId <= 0) {
            Log.e("SaveService", "CategoryId must be valid (> 0).")
            return
        }

        val updatedService = Service(
            id = id,
            categoryId = categoryId,
            description = description,
            discount = discount,
            image = image,
            name = name,
            overalTime = overalTime,
            price = price,
            rating = rating,
            visitors = visitors
        )

        val docRef = db.collection("Services").document(id)

        docRef.set(updatedService, SetOptions.merge())
            .addOnSuccessListener {
                fetchServices()
                Log.d("UpdateService", "Service updated successfully.")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("UpdateService", "Error updating service", e)
                onFailure(e)
            }
    }

    fun deleteService(serviceId: String, onSuccess: () -> Unit = {}) {
        db.collection("Services")
            .document(serviceId)
            .delete()
            .addOnSuccessListener {
                Log.d("ServiceViewModel", "Service deleted successfully")
                // Update the local list
                services = services.filterNot { it.id == serviceId }
                // Call the success callback
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("ServiceViewModel", "Error deleting service", e)
            }
    }

}
