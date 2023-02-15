package fr.kettl.api.domain.entities

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import fr.kettl.api.identity.Snowflake
import java.time.Instant

@Entity
@CqlName("users")
data class User(
    @PartitionKey val id: Snowflake?,
    val firstname: String?,
    val lastname: String?,
    val email: String?,
    val createdDate: Instant?,
)
