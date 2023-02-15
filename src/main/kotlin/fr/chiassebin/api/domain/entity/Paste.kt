package fr.chiassebin.api.domain.entity

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import fr.kettl.api.identity.Snowflake
import kotlinx.serialization.Serializable

/*                id BIGINT PRIMARY KEY,
                  file_name TEXT,
                  file_size INT,
                  bucket_key TEXT,
                  password TEXT,
                  syntax_highlight SMALLINT,
                  tags set<TEXT>,
                  created_at TIMESTAMP
                  expires_at TIMESTAMP
 */
@Entity
@CqlName("pastes")
@Serializable
data class Paste(
    val id: String?,
    val fileName: String?,
    val fileSize: Int?,
    val bucketKey: String?,
    val password: String?,
    val syntaxHighlight: Short?,
    val tags: Set<String>?,
    val createdAt: Long?,
    val expiresAt: Long?
)