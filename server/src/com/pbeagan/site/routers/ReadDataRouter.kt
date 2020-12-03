package com.pbeagan.site.routers

import com.pbeagan.models.worldstate.WorldState
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.koin.java.KoinJavaComponent.inject
import com.pbeagan.site.Router
import com.pbeagan.site.controllers.ShowWorldStateCommand

class ReadDataRouter : Router() {
    override fun get(): Routing.() -> Unit = {
        val model by inject(WorldState::class.java)
        get("/worldstate", ShowWorldStateCommand(model).execute())
    }
}
