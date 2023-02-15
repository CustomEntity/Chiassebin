package fr.chiassebin.web.responses

import kotlinx.serialization.Serializable

@Serializable
data class FineResponse<T>(
    override val code: Int,
    val data: T? = null
) : Response()