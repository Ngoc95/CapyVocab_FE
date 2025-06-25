package com.example.capyvocab_fe.user.notification.data.source

import android.util.Log
import com.example.capyvocab_fe.user.notification.data.dto.NotificationDto
import com.google.gson.Gson
import io.socket.client.Socket
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import javax.inject.Inject

class NotificationSocketDataSource @Inject constructor(
    private val socket: Socket?,
    private val gson: Gson
) {
    private val TAG = "NotificationSocket"

    /**
     * Connect to the socket server and register the user
     */
    fun connect(userId: String) {
        socket?.let { socket ->
            if (!socket.connected()) {
                socket.connect()
                socket.emit("register", userId)

                socket.on(Socket.EVENT_CONNECT) {
                    Log.d("SOCKET", "Socket connected")
                }

                socket.on(Socket.EVENT_DISCONNECT) {
                    Log.d("SOCKET", "Socket disconnected")
                }

                socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                    Log.e("SOCKET", "Socket connection error: ${args.firstOrNull()}")
                }
            }
        }
    }

    /**
     * Disconnect from the socket server
     */
    fun disconnect() {
        socket?.disconnect()
    }

    /**
     * Observe real-time notifications from the socket
     */
    fun observeNotifications(): Flow<NotificationDto> = callbackFlow {
        val onNotification = { args: Array<Any> ->
            if (args.isNotEmpty()) {
                try {
                    val jsonData = args[0] as JSONObject
                    Log.d("SOCKET", "Received notification: $jsonData")
                    val notification = gson.fromJson(jsonData.toString(), NotificationDto::class.java)
                    trySend(notification)
                } catch (e: Exception) {
                    Log.e("SOCKET", "Error parsing notification: ${e.message}")
                }
            }
        }

        socket?.on("notification", onNotification)

        awaitClose {
            socket?.off("notification", onNotification)
        }
    }
}