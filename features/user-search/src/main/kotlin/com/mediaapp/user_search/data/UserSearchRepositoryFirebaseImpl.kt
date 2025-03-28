package com.mediaapp.user_search.data

import com.mediaapp.user_search.domain.UserData
import com.mediaapp.user_search.domain.UserSearchRepository

class UserSearchRepositoryFirebaseImpl(private val storage: UserSearchStorage) :
    UserSearchRepository {

    override suspend fun findUser(prefix: String): List<UserData> {
        return storage.findUsersByPrefix(prefix)
    }
}