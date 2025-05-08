package com.example.spa_ower_app

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

    private val _loginState = MutableStateFlow<Result<FirebaseUser>?>(null)
    val loginState: StateFlow<Result<FirebaseUser>?> = _loginState

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    _loginState.value = Result.success(user!!)
                } else {
                    _loginState.value = Result.failure(task.exception ?: Exception("Unknown error"))
                }
            }
    }

    fun signOut() {
        auth.signOut()
        authState = null
    }

    fun fetchUserData(userID: String, onResult: (User?) -> Unit) {

        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child(userID)

        userRef.get()
            .addOnSuccessListener { dataSnapshot ->
                var user = dataSnapshot.getValue(User::class.java)
                user = user?.copy(uid = userID)
                onResult(user)
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error fetching user", exception)
                onResult(null)
            }
    }
}
