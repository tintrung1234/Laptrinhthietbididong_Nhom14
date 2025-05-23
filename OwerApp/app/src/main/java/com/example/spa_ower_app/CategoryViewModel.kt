package com.example.spa_ower_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class CategoryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var categoriesID by mutableStateOf<List<Int>>(emptyList())
        private set
    var categoriesName by mutableStateOf<List<String>>(emptyList())
        private set

    init {
        fetchCategoryID()
    }

    private fun fetchCategoryID() {
        db.collection("Categories")
            .get()
            .addOnSuccessListener { result ->
                categoriesID = result.mapNotNull { it.id.toIntOrNull()  }
                categoriesName = result.mapNotNull { it.getString("Name") ?: ""  }
            }
            .addOnFailureListener {
                    exception ->
                Log.e("CategoryViewModel", "Error getting categories", exception)
            }
    }

    fun getCategoryIDByName(name: String, onResult: (Int?) -> Unit) {
        db.collection("Categories")
            .whereEqualTo("Name", name)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val id = documents.first().id.toIntOrNull()
                    onResult(id)
                } else {
                    onResult(null) // No category found with that name
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CategoryViewModel", "Error fetching category by name", exception)
                onResult(null)
            }
    }

}