package org.six.series.application.usecases

import org.six.series.model.IUserRepository

class LogOutUseCase (private val userRepository: IUserRepository) {

    suspend fun logout(): Result<Unit> {
        try {
            userRepository.logoutUser()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }

    }
}