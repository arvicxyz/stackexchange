package com.startapplab.stackexchange.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsersResponse(
    @Json(name = "items") val items: List<UserDto>,
    @Json(name = "has_more") val hasMore: Boolean,
    @Json(name = "quota_max") val quotaMax: Int,
    @Json(name = "quota_remaining") val quotaRemaining: Int
)

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "user_id") val userId: Int,
    @Json(name = "display_name") val displayName: String,
    @Json(name = "reputation") val reputation: Int,
    @Json(name = "profile_image") val profileImage: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "creation_date") val creationDate: Long?,
    @Json(name = "last_access_date") val lastAccessDate: Long?,
    @Json(name = "website_url") val websiteUrl: String?,
    @Json(name = "link") val link: String?
)
