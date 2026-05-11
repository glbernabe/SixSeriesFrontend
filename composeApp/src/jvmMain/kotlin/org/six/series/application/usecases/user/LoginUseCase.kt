package org.six.series.application.usecases.user

import org.six.series.model.user.IUserRepository
import org.six.series.application.commands.LoginCommand
import org.six.series.model.user.UserLogin

class LoginUseCase (
    private val userRepository: IUserRepository
) {
    suspend fun login(command: LoginCommand): Result<Unit> {
        if (command.username.isBlank() || command.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Están vacíos los campos"))
        }


        val userLogin = UserLogin(
            username = command.username,
            password = command.password
        )

        userRepository.loginUser(userLogin)
        return Result.success(Unit)
    }
}
