package fr.chiassebin.web.responses

import kotlinx.serialization.Serializable

@Serializable
abstract class Response {
    abstract val code: Int
}