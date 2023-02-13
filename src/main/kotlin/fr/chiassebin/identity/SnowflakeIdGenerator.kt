package fr.kettl.identity

import fr.kettl.api.identity.Snowflake
import java.net.NetworkInterface
import java.security.SecureRandom
import java.time.Instant

class SnowflakeIdGenerator(private val nodeId: Long, private val customEpoch: Long) {

    companion object {
        private const val UNUSED_BITS = 1
        private const val EPOCH_BITS = 41
        private const val NODE_ID_BITS = 10
        private const val SEQUENCE_BITS = 12

        private const val maxNodeId = (1L shl NODE_ID_BITS) - 1
        private const val maxSequence = (1L shl SEQUENCE_BITS) - 1

        private const val DEFAULT_CUSTOM_EPOCH = 1420070400000L

        private fun createNodeId(): Long {
            var nodeId: Long = try {
                val sb = StringBuilder()
                val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                while (networkInterfaces.hasMoreElements()) {
                    val networkInterface = networkInterfaces.nextElement()
                    val mac = networkInterface.hardwareAddress
                    if (mac != null) {
                        for (macPort in mac) {
                            sb.append(String.format("%02X", macPort))
                        }
                    }
                }
                sb.toString().hashCode().toLong()
            } catch (ex: Exception) {
                SecureRandom().nextLong()
            }
            nodeId = nodeId and maxNodeId
            return nodeId
        }
    }

    private var lastTimestamp = -1L
    private var sequence = 0L

    constructor(nodeId: Long) : this(nodeId, DEFAULT_CUSTOM_EPOCH)

    constructor() : this(createNodeId(), DEFAULT_CUSTOM_EPOCH)

    @Synchronized
    fun nextId(): Snowflake {
        var currentTimestamp = timestamp()
        check(currentTimestamp >= lastTimestamp) { "Invalid System Clock!" }
        if (currentTimestamp == lastTimestamp) {
            sequence = sequence + 1 and maxSequence
            if (sequence == 0L) {
                currentTimestamp = waitNextMillis(currentTimestamp)
            }
        } else {
            sequence = 0
        }
        lastTimestamp = currentTimestamp
        return SnowflakeId((currentTimestamp shl NODE_ID_BITS + SEQUENCE_BITS or (nodeId shl SEQUENCE_BITS)
            or sequence))
    }

    private fun timestamp(): Long {
        return Instant.now().toEpochMilli() - customEpoch
    }

    private fun waitNextMillis(currentTimestamp: Long): Long {
        var timestamp = currentTimestamp

        while (timestamp == lastTimestamp) {
            timestamp = timestamp()
        }
        return timestamp
    }

    fun parse(id: Long): LongArray {
        val maskNodeId = (1L shl NODE_ID_BITS) - 1 shl SEQUENCE_BITS
        val maskSequence = (1L shl SEQUENCE_BITS) - 1
        val timestamp = (id shr NODE_ID_BITS + SEQUENCE_BITS) + customEpoch
        val nodeId = id and maskNodeId shr SEQUENCE_BITS
        val sequence = id and maskSequence
        return longArrayOf(timestamp, nodeId, sequence)
    }

    override fun toString(): String {
        return (
            "Snowflake Settings [EPOCH_BITS=$EPOCH_BITS" + ", NODE_ID_BITS=" + NODE_ID_BITS + ", SEQUENCE_BITS=" + SEQUENCE_BITS + ", CUSTOM_EPOCH=" + customEpoch +
                ", NodeId=" + nodeId + "]"
            )
    }
}
