package com.mediaapp.user_search.domain

interface UserSearchRepository {
    suspend fun findUser(prefix: String): List<UserData>
}