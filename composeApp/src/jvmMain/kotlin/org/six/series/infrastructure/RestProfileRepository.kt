package org.six.series.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import org.six.series.model.profile.IProfileRepository
import org.six.series.model.profile.Profile
import org.six.series.model.profile.ProfileUpdateRequest

class RestProfileRepository(
    private val url: String,
    private val cliente: HttpClient,
    private val tokenStorage: TokenStorage
) : IProfileRepository {

    override suspend fun getMyProfiles(): Result<List<Profile>> {
        return try {
            val profiles = cliente.get("$url/") {
                contentType(ContentType.Application.Json)
            }.body<List<Profile>>()
            Result.success(profiles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(id: String, request: ProfileUpdateRequest): Result<Profile> {
        return try {
            val profile = cliente.patch("$url/$id/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<Profile>()
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadAvatar(id: String, imageBytes: ByteArray, mimeType: String): Result<String> {
        return try {
            val response = cliente.post("$url/$id/avatar/") {
                setBody(MultiPartFormDataContent(
                    formData {
                        append("avatar", imageBytes, Headers.build {
                            append(HttpHeaders.ContentType, mimeType)
                            append(HttpHeaders.ContentDisposition, "filename=\"avatar.jpg\"")
                        })
                    }
                ))
            }.body<Map<String, String>>()
            val avatarUrl = response["avatar_url"] ?: error("No avatar_url in response")
            Result.success(avatarUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun createProfile(name: String, color: String): Result<Profile> {
        return try {
            val profile = cliente.post("$url/") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("name" to name, "color" to color))
            }.body<Profile>()
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
