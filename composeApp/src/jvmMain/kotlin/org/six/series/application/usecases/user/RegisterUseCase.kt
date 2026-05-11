package org.six.series.application.usecases.user

import org.six.series.application.commands.RegisterCommand

import org.six.series.model.user.IUserRepository
import org.six.series.model.user.UserRegister

class RegisterUseCase(private val userRepository: IUserRepository) {
    // Si ocurre un error devuelve el texto de abajo, si no, el UI propio se encargará de mostrar un mensaje
    suspend fun register(command: RegisterCommand): Result<Unit> {
        if (command.username.isBlank() || command.email.isBlank() || command.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Todos los campos son obligatorios"))
        }


        //Crear el usuario desde la información del registro y después el servidor añadirá todos los demás elementos UUID, Hashpassword, etc

        val newUser = UserRegister(
            username = command.username,
            email = command.email,
            password = command.password
        )

        userRepository.signupUser(newUser)

        return Result.success(Unit)
    }
}