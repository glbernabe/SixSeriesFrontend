package org.six.series.application.usecases

import org.six.series.model.IUserRepository
import org.six.series.application.commands.LoginCommand
import org.six.series.model.UserLogin

class LoginUseCase (
    private val userRepository: IUserRepository
) {
    suspend fun login(command: LoginCommand): Result<Unit> {
        if (command.email.isBlank() || command.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Están vacíos los campos"))
        }


        val userLogin = UserLogin(
            email = command.email,
            password = command.password
        )

        userRepository.loginUser(userLogin)
        return Result.success(Unit)
    }
}
