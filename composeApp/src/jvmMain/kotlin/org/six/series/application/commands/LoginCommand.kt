package org.six.series.application.commands

data class LoginCommand(
    val username: String,
    val password: String
)