package fr.chiassebin.domain.dao

import com.datastax.oss.driver.api.core.cql.PreparedStatement
import fr.chiassebin.api.domain.dao.PasteDao
import fr.chiassebin.api.domain.entity.Paste
import fr.chiassebin.database.ScyllaDBGateway
import fr.kettl.api.database.ScyllaGateway
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant


class PasteDaoImpl : KoinComponent, PasteDao {

    private val scyllaGateway: ScyllaGateway by inject()

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun findById(id: String?): Deferred<Paste?> = GlobalScope.async {
        val session = scyllaGateway.getSession()
        val preparedStatement: PreparedStatement = session.prepare("SELECT * FROM chiassebin.pastes WHERE id = ?");
        val resultSet = session.execute(preparedStatement.bind(id))
        val row = resultSet.one()
        if (row != null) {
            Paste(
                id = row.getString("id"),
                fileName = row.getString("file_name"),
                fileSize = row.getInt("file_size"),
                bucketKey = row.getString("bucket_key"),
                password = row.getString("password"),
                syntaxHighlight = row.getShort("syntax_highlight"),
                tags = row.getSet("tags", String::class.java),
                createdAt = row.getInstant("created_at")?.toEpochMilli(),
                expiresAt = row.getInstant("expires_at")?.toEpochMilli()
            )
        } else null
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun save(paste: Paste): Deferred<Void?> = GlobalScope.async {
        val session = scyllaGateway.getSession()
        val preparedStatement: PreparedStatement = session.prepare("INSERT INTO chiassebin.pastes (id, file_name, file_size, bucket_key, password, syntax_highlight, tags, created_at, expires_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        session.execute(preparedStatement.bind(
            paste.id,
            paste.fileName,
            paste.fileSize,
            paste.bucketKey,
            paste.password,
            paste.syntaxHighlight,
            paste.tags,
            paste.createdAt?.let { Instant.ofEpochMilli(it) },
            paste.expiresAt?.let { Instant.ofEpochMilli(it)}
        ))
        null
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun exists(id: String?): Deferred<Boolean> = GlobalScope.async {
        val session = scyllaGateway.getSession()
        val preparedStatement: PreparedStatement = session.prepare("SELECT COUNT(*) FROM chiassebin.pastes WHERE id = ?");
        val resultSet = session.execute(preparedStatement.bind(id))
        val row = resultSet.one()
        row != null && row.getLong(0) > 0
    }
}