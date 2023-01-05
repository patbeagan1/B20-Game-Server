package dev.patbeagan.b20.domain.flags

import dev.patbeagan.base.Flag

enum class ContainerState : Flag {
    CLOSEABLE,
    CLOSED,
    LOCKED
}