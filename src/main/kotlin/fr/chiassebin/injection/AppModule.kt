package fr.chiassebin.injection

import fr.chiassebin.ChiasseApplication
import fr.chiassebin.config.Config
import fr.chiassebin.database.ScyllaDBGateway
import fr.chiassebin.web.controllers.UserController
import fr.kettl.api.database.ScyllaGateway
import fr.kettl.identity.SnowflakeIdGenerator
import org.koin.dsl.module

class AppModule {
    companion object {
        val appModule = module {
            single {
                UserController()
            }
            single {
                ChiasseApplication()
            }
            single<ScyllaGateway> { ScyllaDBGateway() }
            single {
                SnowflakeIdGenerator(Config.NODE_ID)
            }
        }
    }
}
