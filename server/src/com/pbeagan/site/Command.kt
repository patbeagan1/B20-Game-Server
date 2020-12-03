package com.pbeagan.site

import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

typealias Pipeline = suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit

abstract class Command {
    abstract fun execute(): Pipeline
}
