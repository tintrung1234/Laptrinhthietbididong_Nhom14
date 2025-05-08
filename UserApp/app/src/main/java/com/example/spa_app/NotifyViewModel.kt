package com.example.spa_app

import android.app.Notification
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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
    val seenByUser: Boolean = false,
    val seenByOwner: Boolean = false,
    val type: String = ""
)

class NotifyViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _notifications = mutableStateListOf<UserNotification>()
    val notifications: List<UserNotification> = _notifications
    private val _updateInforEvents = MutableSharedFlow<UpdateInforNotify>()
    val updateInforEvents = _updateInforEvents.asSharedFlow()
    // Flow để gửi sự kiện hiển thị notification ra ngoài UI
    private val _notificationEvents = MutableSharedFlow<UserNotification>()
    val notificationEvents = _notificationEvents.asSharedFlow()

    //Tạo thông báo khi cập nhật lại thông tin cá nhân
    fun createUpdateInforNotification(userId: String) {

        val notificationId = db.collection("notifications").document().id
        val updateNotification = UpdateInforNotify(
            id = notificationId,
            userId = userId,
            contentForUser = "Bạn đã thay đổi thông tin cá nhân",
            contentForOwner = "",
            timestamp = System.currentTimeMillis(),
            seenByUser = false,
            type = "suathongtin"

        )

        // Lưu thông báo vào Firestore
        db.collection("notifications").document(notificationId)
            .set(updateNotification)
            .addOnSuccessListener {
                Log.d("Notify", "Thông báo cập nhật thông tin đã được tạo")
                // Gửi sự kiện ra UI
                viewModelScope.launch {
                    _updateInforEvents.emit(updateNotification)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Notify", "Lỗi khi tạo thông báo cập nhật thông tin", e)
            }
    }
    // Tạo thông báo mới (gọi từ PaymentScreen)
    fun createNotification(userId: String) {
        val userDocRef = db.collection("Users").document(userId)

        userDocRef.get().addOnSuccessListener { document ->
            val username = document.getString("name") ?: "khách hàng"

            val notificationId = db.collection("notifications").document().id
            val notification = UserNotification(
                id = notificationId,
                userId = userId,
                contentForUser = "Bạn đã đặt lịch thành công!",
                contentForOwner = "Khách hàng $username vừa đặt lịch.",
                timestamp = System.currentTimeMillis(),
                seenByUser = false,
                seenByOwner = false,
                type = "datlich"

            )

            db.collection("notifications").document(notificationId)
                .set(notification)
                .addOnSuccessListener {
                    Log.d("Notify", "Thông báo đã được tạo")
                    // Gửi sự kiện ra UI
                    viewModelScope.launch {
                        _notificationEvents.emit(notification)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Notify", "Lỗi khi tạo thông báo", e)
                }

        }.addOnFailureListener { e ->
            Log.e("Notify", "Lỗi khi lấy tên người dùng", e)
        }
    }

    // Load danh sách thông báo của người dùng (hiển thị trong NotifyScreen)
    fun loadUserNotifications(userId: String) {
        db.collection("notifications")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("NotifyVM", "Lỗi khi tải thông báo", error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    _notifications.clear()
                    // Sửa ở đây để đảm bảo kiểu trả về là Collection<UserNotification>
                    val notificationsList: List<UserNotification> = it.documents.mapNotNull { doc ->
                        doc.toObject(UserNotification::class.java)
                            ?: doc.toObject(UpdateInforNotify::class.java) as UserNotification
                    }
                    _notifications.addAll(notificationsList)
                }
            }
    }
    fun addMissingTypeToNotifications() {
        val db = FirebaseFirestore.getInstance()
        db.collection("notifications")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    if (!doc.contains("seenByOwner")) {
                        db.collection("notifications").document(doc.id)
                            .update("seenByOwner", false) // hoặc "capnhat" tùy thông báo
                            .addOnSuccessListener {
                                Log.d("FixNotify", "Cập nhật type cho ${doc.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.e("FixNotify", "Lỗi cập nhật type cho ${doc.id}", e)
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FixNotify", "Lỗi lấy danh sách thông báo", e)
            }
    }


}