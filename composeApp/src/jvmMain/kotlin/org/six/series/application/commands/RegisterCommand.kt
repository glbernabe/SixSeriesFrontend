package org.six.series.application.commands

data class RegisterCommand(
    val email: String,
    val username: String,
    val password: String

)