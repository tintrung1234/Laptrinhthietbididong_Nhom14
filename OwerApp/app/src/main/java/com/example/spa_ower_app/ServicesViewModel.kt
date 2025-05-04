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
            .addOnFailureListener { exception ->
                Log.e("ServiceViewModel", "Error getting services", exception)
            }
    }

    fun saveService(
        id: String,
        CategoryId: Int,
        Description: String,
        Discount: Int,
        Image: String,
        Name: String,
        OveralTime: Int,
        Price: Float,
        Rating: Int,
        Visitors: Int,
        onSuccess: () -> Unit = {}, // Callback
        onFailure: (Exception) -> Unit = {}
    ) {
        // Validate required fields
        if (Name.isBlank() || Description.isBlank() || Image.isBlank()) {
            Log.e("SaveService", "Name, Description, and Image must not be empty.")
            return
        }

        if (Price <= 0f) {
            Log.e("SaveService", "Price must be greater than 0.")
            return
        }

        if (OveralTime <= 0) {
            Log.e("SaveService", "OveralTime must be greater than 0.")
            return
        }

        if (CategoryId <= 0) {
            Log.e("SaveService", "CategoryId must be valid (> 0).")
            return
        }

        val docId = if (id.isBlank()) db.collection("Services").document().id else id
        val docRef = db.collection("Services").document(docId)

        val newService = Service(
            id = docId,
            CategoryId = CategoryId,
            Description = Description,
            Discount = Discount,
            Image = Image,
            Name = Name,
            OveralTime = OveralTime,
            Price = Price,
            Rating = Rating,
            Visitors = Visitors
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
        CategoryId: Int,
        Description: String,
        Discount: Int,
        Image: String,
        Name: String,
        OveralTime: Int,
        Price: Float,
        Rating: Int,
        Visitors: Int,
        onSuccess: () -> Unit = {}, // Callback
        onFailure: (Exception) -> Unit = {}
    ) {
        if (id.isBlank()) {
            Log.e("UpdateService", "ID is required to update a service.")
            return
        }

        if (Name.isBlank() || Description.isBlank() || Image.isBlank()) {
            Log.e("SaveService", "Name, Description, and Image must not be empty.")
            return
        }

        if (Price <= 0f) {
            Log.e("SaveService", "Price must be greater than 0.")
            return
        }

        if (OveralTime <= 0) {
            Log.e("SaveService", "OveralTime must be greater than 0.")
            return
        }

        if (CategoryId <= 0) {
            Log.e("SaveService", "CategoryId must be valid (> 0).")
            return
        }

        val updatedService = Service(
            id = id,
            CategoryId = CategoryId,
            Description = Description,
            Discount = Discount,
            Image = Image,
            Name = Name,
            OveralTime = OveralTime,
            Price = Price,
            Rating = Rating,
            Visitors = Visitors
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
}
