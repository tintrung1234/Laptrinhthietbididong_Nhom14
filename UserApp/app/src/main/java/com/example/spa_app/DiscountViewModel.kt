package com.example.spa_app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Discount(
    val id: String = "",
    val title: String = "",
    val code: String = "",
    val value: Int = 0,
    val quantity: Int = 0,
)
class DiscountViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var vouchers by mutableStateOf<List<Discount>>(emptyList())
        private set
    init {
        fetchAppointments()
    }
    private fun fetchAppointments() {
        db.collection("discount")
            .get()
            .addOnSuccessListener { result ->
                val newVouchers = mutableListOf<Discount>()
                val newIds = mutableListOf<String>()

                for (doc in result) {
                    val voucher = doc.toObject(Discount::class.java).copy(id = doc.id)
                    newVouchers.add(voucher)
                    newIds.add(doc.id)
                }

                vouchers = newVouchers
            }
            .addOnFailureListener { exception ->
                Log.e("AppointmentViewModel", "Error getting appointments", exception)
            }
    }

    //reduce quantity of voucher
    fun updateVoucherInFirestore(discount: Discount) {
        val db = FirebaseFirestore.getInstance()
        db.collection("discount")
            .document(discount.id)
            .update("quantity", discount.quantity-1)
            .addOnSuccessListener {
                Log.d("Firestore", "Appointment state updated to 1")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating state", e)
            }
    }

}