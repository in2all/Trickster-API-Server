package co.in2all.trickster.api.server.service.impl

import co.in2all.trickster.api.server.entity.User
import co.in2all.trickster.api.server.repository.RefreshPasswordTokensRepository
import co.in2all.trickster.api.server.repository.UsersRepository
import co.in2all.trickster.api.server.service.ChangePasswordService
import com.typesafe.config.ConfigFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class ChangePasswordServiceImpl @Autowired constructor(
        val refreshPasswordTokensRepository: RefreshPasswordTokensRepository,
        val usersRepository: UsersRepository) : ChangePasswordService {

    override fun createToken(email: String, clientId: String?, token: String) {
        val expiresIn = Date().time + ConfigFactory.load().getLong("refresh_password_token.lifetime")

        when (refreshPasswordTokensRepository.getByEmail(email)) {
            null -> refreshPasswordTokensRepository.create(email, token, clientId, expiresIn)
            else -> refreshPasswordTokensRepository.refresh(email, token, clientId, expiresIn)
        }
    }

    override fun changePassword(user: User, newPassword: String) {
        //TODO-misonijnik: Добавь шифрование нового пароля.

        usersRepository.changePassword(user.email!!, newPassword)
    }
}
