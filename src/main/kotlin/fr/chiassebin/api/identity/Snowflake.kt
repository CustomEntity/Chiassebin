package fr.kettl.api.identity

interface Snowflake {
    val id: Long

    fun isSameAs(other: Snowflake): Boolean {
        return id == other.id
    }
}
