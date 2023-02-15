package fr.chiassebin.injection

import fr.chiassebin.ChiasseApplication
import fr.chiassebin.api.domain.dao.PasteDao
import fr.chiassebin.config.Config
import fr.chiassebin.database.ScyllaDBGateway
import fr.chiassebin.domain.dao.PasteDaoImpl
import fr.chiassebin.web.controllers.PasteController
import fr.chiassebin.web.controllers.UserController
import fr.kettl.api.database.ScyllaGateway
import fr.kettl.identity.SnowflakeIdGenerator
import org.koin.dsl.module

class AppModule {
    companion object {
        val appModule = module {
            single<ScyllaGateway> { ScyllaDBGateway() }
            single<PasteDao> { PasteDaoImpl() }
            single {
                UserController()
            }
            single() {
                PasteController()
            }
            single {
                ChiasseApplication()
            }
            single {
                SnowflakeIdGenerator(Config.NODE_ID)
            }
        }
    }
}
