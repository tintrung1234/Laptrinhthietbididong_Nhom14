package com.example.spa_ower_app

import android.app.Notification
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

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

    // Sự kiện notification mới
    private val _notificationEvent = MutableSharedFlow<UserNotification>()
    val notificationEvent = _notificationEvent.asSharedFlow()

    fun loadNotifications(userId: String) {
        db.collection("notifications")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("NotifyVM", "Lỗi khi tải thông báo", error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val newNotifications = it.documents.mapNotNull { doc ->
                        doc.toObject(UserNotification::class.java)
                    }

                    // Kiểm tra và phát thông báo mới (nếu có)
                    if (newNotifications.isNotEmpty() && newNotifications != _notifications) {
                        val latest = newNotifications.first()
                        viewModelScope.launch {
                            _notificationEvent.emit(latest)
                        }
                    }

                    _notifications.clear()
                    _notifications.addAll(newNotifications)
                }
            }
    }
}

