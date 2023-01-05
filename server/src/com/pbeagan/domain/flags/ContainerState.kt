package com.pbeagan.domain.flags

import dev.patbeagan.base.Flag

enum class ContainerState : Flag {
    CLOSEABLE,
    CLOSED,
    LOCKED
}