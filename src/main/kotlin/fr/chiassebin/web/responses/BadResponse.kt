package fr.chiassebin.web.responses

import kotlinx.serialization.Serializable

@Serializable
data class BadResponse(
    override val code: Int,
    val message: String,
) : Response()