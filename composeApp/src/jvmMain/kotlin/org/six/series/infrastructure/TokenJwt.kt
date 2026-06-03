package org.six.series.infrastructure

import kotlinx.serialization.json.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Clock

enum class Permissions (
    val total: String,
    val create: String,
    val edit: String,
    val read: String,
    val none: String
)

data class TokenJwtHeader(val alg: String, val typ: String)

data class TokenJwtPayload(val claims: Map<String, Any> = emptyMap()) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T? = claims[key] as? T

    val userId: String? get() = get("sub")
    val expiration: Long? get() = (claims["exp"] as? Number)?.toLong()
}

data class TokenJwtFirma(val firma: String)

// Structured data representation of the custom claims inside the token
data class UserTokenData(
    val username: String?,
    val role: String?,
    val permissions: String?
)

class TokenJwt(val rawToken: String) {

    val header: TokenJwtHeader
    val payload: TokenJwtPayload
    val firma: TokenJwtFirma

    init {
        val parts = rawToken.split(".")
        if (parts.size != 3) {
            throw IllegalArgumentException("Formato de token inválido. Debe tener 3 partes.")
        }
        header = decodeHeader(parts[0])
        payload = decodePayload(parts[1])
        firma = TokenJwtFirma(parts[2])
    }

    /**
     * Extracts and structures the key claims from the decoded payload for UI consumption.
     */
    fun getUserData(): UserTokenData {
        return UserTokenData(
            username = payload.get<String>("sub"),
            role = payload.get<String>("role"),
            permissions = payload.get<String>("permissions")
        )
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun decodeHeader(base64Header: String): TokenJwtHeader {
        val jsonString = Base64.UrlSafe.decode(base64Header).decodeToString()
        val json = Json.parseToJsonElement(jsonString).jsonObject

        return TokenJwtHeader(
            alg = json["alg"]?.jsonPrimitive?.content ?: "HS256",
            typ = json["typ"]?.jsonPrimitive?.content ?: "JWT"
        )
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun decodePayload(base64Payload: String): TokenJwtPayload {
        val decoder = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT_OPTIONAL)
        val jsonString = decoder.decode(base64Payload).decodeToString()
        val jsonElement = Json.parseToJsonElement(jsonString).jsonObject

        val claimsMap = jsonElement.mapValues { (_, value) ->
            value.toPrimitive()
        }
        return TokenJwtPayload(claimsMap)
    }

    private fun JsonElement.toPrimitive(): Any {
        return when (this) {
            is JsonPrimitive -> {
                if (isString) content
                else if (booleanOrNull != null) booleanOrNull!!
                else if (longOrNull != null) longOrNull!!
                else doubleOrNull!!
            }
            is JsonArray -> map { it.toPrimitive() }
            is JsonObject -> mapValues { it.value.toPrimitive() }
            else -> toString()
        }
    }

    fun isSessionValid(): Boolean {
        return this.payload.get<Long>("exp")?.let {
            it > Clock.System.now().epochSeconds
        } ?: false
    }
}