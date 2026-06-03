package org.six.series.application.usecases.user

import org.six.series.model.user.IUserRepository
import org.six.series.model.user.UserAccount

class GetAllUsersUseCase(private val userRepository: IUserRepository) {
    suspend operator fun invoke(): Result<List<UserAccount>> {
        return try {
            val users = userRepository.getAllUsers()
            if (users.isEmpty()) {
                Result.failure(Exception("No hay usuarios registrados en el sistema"))
            } else {
                Result.success(users)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}