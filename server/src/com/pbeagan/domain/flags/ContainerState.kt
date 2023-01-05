package com.pbeagan.domain.flags

import com.pbeagan.util.Flag

enum class ContainerState : Flag {
    CLOSEABLE,
    CLOSED,
    LOCKED
}