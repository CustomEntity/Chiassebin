package fr.chiassebin.api.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class PostPaste(
    val fileName: String? = null,
    val password: String? = null,
    val syntaxHighlight: Short? = null,
    val tags: Set<String>? = null,
    val expiresAt: Long? = null,
    val content: String? = null
)