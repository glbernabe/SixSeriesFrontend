package org.six.series.model

interface IUserRepository {
    suspend fun loginUser(user: UserLogin)
    suspend fun signupUser(user: UserRegister)
    suspend fun logoutUser()
}