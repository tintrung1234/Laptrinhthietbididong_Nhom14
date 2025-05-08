package com.example.spa_app

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

data class Contact(
    val name: String = "",
    val email: String = "",
    val message: String = ""
)
class ContactViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun submitFeedback(contact: Contact, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val feedback = hashMapOf(
            "name" to contact.name,
            "email" to contact.email,
            "message" to contact.message,
            "timestamp" to FieldValue.serverTimestamp(),
            "emailed" to false
        )
        db.collection("contacts")
            .add(feedback)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }
}