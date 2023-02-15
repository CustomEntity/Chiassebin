package fr.chiassebin.api.domain.dao

import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.Select
import fr.chiassebin.api.domain.entity.Paste
import fr.kettl.api.domain.entities.User
import fr.kettl.api.identity.Snowflake
import kotlinx.coroutines.Deferred

interface PasteDao {

    suspend fun findById(
        id: String?
    ): Deferred<Paste?>

    suspend fun save(
        paste: Paste
    ): Deferred<Void?>

    suspend fun exists(
        id: String?
    ): Deferred<Boolean>

}