package site.routers

import com.pbeagan.models.worldstate.WorldState
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.koin.java.KoinJavaComponent.inject
import site.Router
import site.controllers.ShowWorldStateCommand

class ReadDataRouter : Router() {
    override fun get(): Routing.() -> Unit = {
        val model by inject(WorldState::class.java)
        get("/worldstate", ShowWorldStateCommand(model).execute())
    }
}
