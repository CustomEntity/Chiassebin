package fr.chiassebin.database

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.PreparedStatement
import fr.chiassebin.config.Config
import fr.chiassebin.database.codecs.SnowflakeTypeCodec
import fr.kettl.api.database.ScyllaGateway
import fr.kettl.api.domain.mapper.UserMapper
import java.net.InetSocketAddress
import kotlin.system.exitProcess

class ScyllaDBGateway : ScyllaGateway {

    private lateinit var cqlSession: CqlSession
    private lateinit var userMapper: UserMapper

    override fun getUserMapper(): UserMapper {
        return userMapper
    }

    override fun init() {
        try {
            this.cqlSession = CqlSession.builder()
                //.addContactPoint(InetSocketAddress(Config.SCYLLA_HOST, Config.SCYLLA_PORT))
                .withAuthCredentials(Config.SCYLLA_USER, Config.SCYLLA_PASSWORD)
                .addTypeCodecs(
                    SnowflakeTypeCodec()
                )
                .build()
            //userMapper = UserMapper.builder(this.cqlSession).withDefaultKeyspace(Config.SCLLA_DEFAULT_KEYSPACE).build()
        } catch (e: Exception) {
            println("Error while connecting to ScyllaDB")
            e.printStackTrace()
            exitProcess(1)
        }
    }

    override fun start() {
        val stmt: PreparedStatement =
            this.cqlSession.prepare("CREATE KEYSPACE IF NOT EXISTS chiassebin WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};")
        val stmt2: PreparedStatement =
            this.cqlSession.prepare("CREATE TABLE IF NOT EXISTS chiassebin.pastes (id TEXT PRIMARY KEY, file_name TEXT, file_size INT, bucket_key TEXT, password TEXT, syntax_highlight SMALLINT, tags set<TEXT>, created_at TIMESTAMP, expires_at TIMESTAMP);")
        this.cqlSession.execute(stmt.bind())
        this.cqlSession.execute(stmt2.bind())
    }

    override fun getSession(): CqlSession {
        return this.cqlSession
    }
}
