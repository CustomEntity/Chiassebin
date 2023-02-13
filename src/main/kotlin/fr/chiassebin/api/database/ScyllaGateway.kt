package fr.kettl.api.database

import com.datastax.oss.driver.api.core.CqlSession
import fr.kettl.api.domain.mapper.UserMapper

interface ScyllaGateway : DatabaseGateway {
    fun getUserMapper(): UserMapper

    fun getSession(): CqlSession
}
