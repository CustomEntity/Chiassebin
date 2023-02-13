package fr.kettl.api.domain.mapper

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.MapperBuilder
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.Mapper
import fr.kettl.api.domain.dao.UserDao

interface UserMapper {

    @DaoFactory
    fun userDao(): UserDao
}
