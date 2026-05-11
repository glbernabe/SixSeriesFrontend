package org.six.series.model.user

interface IUserRepository {
    suspend fun loginUser(user: UserLogin)
    suspend fun signupUser(user: UserRegister)
    suspend fun logoutUser()
}