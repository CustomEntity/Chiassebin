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
    val SCYLLA_DEFAULT_KEYSPACE: String = env["SCYLLA_DEFAULT_KEYSPACE"] ?: "kettl"
    val AWS_ENDPOINT: String = env["AWS_ENDPOINT"] ?: "https://s3.tebi.io"
    val AWS_REGION: String = env["AWS_REGION"] ?: "global"
    val AWS_BUCKET_NAME: String = env["AWS_BUCKET_NAME"] ?: "chiassebin"
}
