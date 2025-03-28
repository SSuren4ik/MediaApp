package com.mediaapp.user_search.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.mediaapp.user_search.domain.UserData
import kotlinx.coroutines.tasks.await

class UserSearchStorageFirebaseImpl(
    private val firebaseDatabase: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
) : UserSearchStorage {

    override suspend fun findUsersByPrefix(prefix: String): List<UserData> {
        return try {
            val usersRef = firebaseDatabase.child("Users")
            val snapshot = usersRef.get().await()
            if (!snapshot.exists()) emptyList() else filterUsers(snapshot, prefix)
        } catch (e: Exception) {
            throw e
        }
    }

    private fun filterUsers(snapshot: DataSnapshot, prefix: String): List<UserData> {
        val currentUserId = firebaseAuth.currentUser!!.uid

        return snapshot.children.mapNotNull { userSnapshot ->
            val userId = userSnapshot.key
            val username = userSnapshot.child("username").getValue(String::class.java)

            if (userId != null && userId != currentUserId && username != null && username.startsWith(
                    prefix, ignoreCase = true
                )
            ) {
                UserData(name = username, id = userId)
            } else {
                null
            }
        }
    }
}