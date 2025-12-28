package com.startapplab.stackexchange.domain.usecase

import com.startapplab.stackexchange.data.repository.UserRepository
import com.startapplab.stackexchange.domain.model.User
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(query: String, page: Int = 1, pageSize: Int = 20): Result<List<User>> {
        return userRepository.searchUsers(query = query, page = page, pageSize = pageSize)
    }
}
