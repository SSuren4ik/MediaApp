package com.mediaapp.user_search.data

import com.mediaapp.user_search.domain.UserData

interface UserSearchStorage {
    suspend fun findUsersByPrefix(prefix: String): List<UserData>
}