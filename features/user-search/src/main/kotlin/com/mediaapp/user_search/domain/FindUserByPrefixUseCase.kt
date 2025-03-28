package com.mediaapp.user_search.domain

class FindUserByPrefixUseCase(private val repository: UserSearchRepository) {

    suspend fun execute(prefix: String): List<UserData> {
        return repository.findUser(prefix)
    }
}