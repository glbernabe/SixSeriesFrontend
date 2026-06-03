package org.six.series.application.usecases.user

import org.six.series.model.user.IUserRepository

class DeleteUserByIdUseCase(private val userRepository: IUserRepository) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return try {
            val success = userRepository.deleteUserById(userId)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("El usuario no existe o no se pudo eliminar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}