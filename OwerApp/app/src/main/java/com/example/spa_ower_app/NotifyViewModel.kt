package com.example.spa_ower_app

import android.app.Notification
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

data class UserNotification(
    val id: String = "",
    val userId: String = "",
    val contentForUser: String = "",
    val contentForOwner: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val seenByOwner: Boolean = false
)


class NotifyViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _notifications = mutableStateListOf<UserNotification>()
    val notifications: List<UserNotification> = _notifications

    // Tải lại thông báo từ Firestore cho UserApp và OwnerApp
    fun loadNotifications(userId: String) {

        db.collection("notifications")
            .whereEqualTo("userId", userId) // Đây là userId của cả UserApp và OwnerApp
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("NotifyVM", "Lỗi khi tải thông báo", error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    _notifications.clear()
                    _notifications.addAll(
                        it.documents.mapNotNull { doc ->
                            doc.toObject(UserNotification::class.java)
                        }
                    )
                }
            }
    }
}


