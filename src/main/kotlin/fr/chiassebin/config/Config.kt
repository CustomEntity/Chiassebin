package fr.chiassebin.config

import java.io.File

object Config {
    private val env: Map<String, String>

    init {
        val envFile = File(".env")
        env = if (envFile.exists()) {
            envFile.inputStream()
                .bufferedReader()
                .readLines()
                .map {
                    it.split(
                        "=",
                    )
                }
                .associate { (name, value) -> name to value }
        } else {
            System.err.println("No .env file found.")
            emptyMap()
        }
    }

    val NODE_ID: Long = env["NODE_ID"]?.toLong() ?: 0L
    val SCYLLA_HOST: String = env["SCYLLA_HOST"] ?: "localhost"
    val SCYLLA_PORT: Int = env["SCYLLA_PORT"]?.toInt() ?: 9042
    val SCYLLA_USER: String = env["SCYLLA_USER"] ?: "cassandra"
    val SCYLLA_PASSWORD: String = env["SCYLLA_PASSWORD"] ?: "cassandra"
    val SCYLLA_DEFAULT_KEYSPACE: String = env["SCLLA_DEFAULT_KEYSPACE"] ?: "kettl"
}
