package fr.chiassebin.util

class IdGenerator {
    companion object {

        private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        fun generateId(length: Int): String {
            return (1..length)
                .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
        }
    }
}