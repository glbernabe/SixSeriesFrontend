package org.six.series.application.usecases.user

import org.six.series.model.user.IUserRepository

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