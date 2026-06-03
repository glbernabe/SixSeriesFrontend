package org.six.series.model.user

interface IUserRepository {
    suspend fun loginUser(user: UserLogin)
    suspend fun signupUser(user: UserRegister)
    suspend fun logoutUser()
    suspend fun getAllUsers(): List<UserAccount>
    suspend fun updateUserAccount(updatedUser: UserAccount): String
    suspend fun updateUserStatus(userId: String, status: Boolean): Boolean
    suspend fun deleteUserById(userId: String): Boolean
}