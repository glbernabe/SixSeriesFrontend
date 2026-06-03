package org.six.series.application.usecases.user

import org.six.series.model.user.IUserRepository
import org.six.series.model.user.UserAccount

class UpdateUserStatusUseCase(private val userRepository: IUserRepository) {
    suspend operator fun invoke(idUser: String): Result<List<UserAccount>> {
        return try {
            val users = userRepository.getAllUsers()
            val currentUser = users.find { it.id == idUser } ?: return Result.failure(Exception("Not found"))

            userRepository.updateUserStatus(idUser, !currentUser.isActive)

            Result.success(userRepository.getAllUsers())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}