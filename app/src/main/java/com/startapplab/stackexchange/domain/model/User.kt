package com.startapplab.stackexchange.domain.model

data class User(
    val id: Int,
    val username: String,
    val reputation: Int,
    val profileImage: String?,
    val location: String?,
    val creationDate: String?,
    val lastAccessDate: String?,
    val websiteUrl: String?,
    val badgeCounts: BadgeCounts?,
    val reputationChangeYear: Int?,
    val reputationChangeMonth: Int?,
    val reputationChangeWeek: Int?,
    val reputationChangeDay: Int?
)

data class BadgeCounts(
    val gold: Int,
    val silver: Int,
    val bronze: Int
)
