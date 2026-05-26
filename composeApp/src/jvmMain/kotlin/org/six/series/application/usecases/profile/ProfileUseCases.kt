package org.six.series.application.usecases.profile

import org.six.series.model.profile.IProfileRepository
import org.six.series.model.profile.Profile
import org.six.series.model.profile.ProfileUpdateRequest

class GetMyProfilesUseCase(private val repo: IProfileRepository) {
    suspend operator fun invoke(): Result<List<Profile>> = repo.getMyProfiles()
}

class UpdateProfileUseCase(private val repo: IProfileRepository) {
    suspend operator fun invoke(id: String, request: ProfileUpdateRequest): Result<Profile> =
        repo.updateProfile(id, request)
}

class UploadAvatarUseCase(private val repo: IProfileRepository) {
    suspend operator fun invoke(id: String, imageBytes: ByteArray, mimeType: String): Result<String> =
        repo.uploadAvatar(id, imageBytes, mimeType)
}
class CreateProfileUseCase(private val repo: IProfileRepository) {
    suspend operator fun invoke(name: String, color: String = "#6A6A69"): Result<Profile> =
        repo.createProfile(name, color)
}