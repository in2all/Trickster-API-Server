package co.in2all.trickster.api.server.utility
import com.typesafe.config.ConfigFactory
import org.springframework.security.crypto.password.StandardPasswordEncoder
import java.util.UUID

object Safeguard {
    private val encoder = StandardPasswordEncoder(ConfigFactory.load().getString("security.secret"))
    fun getUniqueToken() = UUID.randomUUID().toString().split("-").joinToString("")
    fun encodePassword(password: String) = encoder.encode(password)
    fun assertPassword(password: String, encodedPassword : String) = encoder.matches(password, encodedPassword)
}
