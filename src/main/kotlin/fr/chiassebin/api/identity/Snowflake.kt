package fr.kettl.api.identity

interface Snowflake {

    companion object {
        val SNOWFLAKE_REGEX = Regex("^[0-9]{19,20}\$")
    }

    val id: Long

    fun isSameAs(other: Snowflake): Boolean {
        return id == other.id
    }
}
