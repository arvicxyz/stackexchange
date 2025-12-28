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
    @Json(name = "badge_counts") val badgeCounts: BadgeCountsDto?,
    @Json(name = "reputation_change_year") val reputationChangeYear: Int?,
    @Json(name = "reputation_change_quarter") val reputationChangeQuarter: Int?,
    @Json(name = "reputation_change_month") val reputationChangeMonth: Int?,
    @Json(name = "reputation_change_week") val reputationChangeWeek: Int?,
    @Json(name = "reputation_change_day") val reputationChangeDay: Int?
)

@JsonClass(generateAdapter = true)
data class BadgeCountsDto(
    @Json(name = "gold") val gold: Int,
    @Json(name = "silver") val silver: Int,
    @Json(name = "bronze") val bronze: Int
)
