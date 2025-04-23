package com.example.spa_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel() {
    data class User(
        val uid: String = "",
        val name: String = "",
        val phone: String = "",
        val email: String = ""
    )

    fun saveUserToRealtimeDatabase(uid: String, name: String, phone: String, email: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(uid)

        val user = User(uid, name, phone, email)

        userRef.setValue(user)
            .addOnSuccessListener {
                Log.d("Firebase", "User saved successfully")
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error saving user", e)
            }
    }

    private val auth = FirebaseAuth.getInstance()

    var authState by mutableStateOf<FirebaseUser?>(auth.currentUser)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun signUpWithDetails(email: String, password: String, username: String, phone: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                saveUserToRealtimeDatabase(uid, username, phone, email)
            }
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                authState = if (task.isSuccessful) auth.currentUser else null
                if (!task.isSuccessful) errorMessage = task.exception?.message
            }
    }

    fun signOut() {
        auth.signOut()
        authState = null
    }
}
