package org.six.series.application.usecases.user

import org.six.series.model.user.IUserRepository
import org.six.series.model.user.UserAccount

class UpdateUserAccountUseCase(private val userRepository: IUserRepository) {
    suspend operator fun invoke(updatedUser: UserAccount): Result<String> {
        return try {
            val message = userRepository.updateUserAccount(updatedUser)
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}