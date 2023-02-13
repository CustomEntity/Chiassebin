package fr.kettl.identity

import fr.kettl.api.identity.Snowflake

data class SnowflakeId(override val id: Long) : Snowflake
