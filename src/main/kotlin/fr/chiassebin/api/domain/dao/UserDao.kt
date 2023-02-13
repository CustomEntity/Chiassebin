package fr.kettl.api.domain.dao

import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.Select
import fr.kettl.api.domain.entities.User
import fr.kettl.api.identity.Snowflake
import kotlinx.coroutines.Deferred

interface UserDao {

    @Select
    suspend fun findById(
        id: Snowflake?
    ): Deferred<User?>

    @Insert
    suspend fun save(
        user: User
    ): Deferred<Void?>
}
