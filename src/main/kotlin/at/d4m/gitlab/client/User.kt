package at.d4m.gitlab.client

import java.util.*

data class User(
        val id: Long,
        val name: String,
        val username: String,
        val state: String,
        val avatar_url: String,
        val web_url: String,
        val created_at: Date,
        val bio: String?,
        val location: String?,
        val skype: String,
        val linkedin: String,
        val twitter: String,
        val website_url: String,
        val organization: String?,
        val last_sign_in_at: Date,
        val confirmed_at: Date,
        val last_activity_on: String?,
        val email: String,
        val theme_id: Int,
        val color_scheme_id: Int,
        val projects_limit: Long,
        val current_sign_in_at: String,
        val identities: List<String>,
        val can_create_group: Boolean,
        val can_create_project: Boolean,
        val two_factor_enabled: Boolean,
        val external: Boolean,
        val is_admin: Boolean
)