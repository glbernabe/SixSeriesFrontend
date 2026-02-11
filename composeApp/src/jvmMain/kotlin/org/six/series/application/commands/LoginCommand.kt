package org.six.series.application.commands

data class LoginCommand(
    val email: String,
    val password: String
)