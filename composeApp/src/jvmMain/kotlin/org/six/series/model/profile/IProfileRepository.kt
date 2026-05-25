package org.six.series.model.profile

interface IProfileRepository {
    suspend fun getMyProfiles(): Result<List<Profile>>
    suspend fun createProfile(name: String, color: String): Result<Profile>
    suspend fun updateProfile(id: String, request: ProfileUpdateRequest): Result<Profile>
    suspend fun uploadAvatar(id: String, imageBytes: ByteArray, mimeType: String): Result<String>
}