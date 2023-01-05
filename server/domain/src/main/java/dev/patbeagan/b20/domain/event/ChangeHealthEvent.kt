package dev.patbeagan.b20.domain.event

class ChangeHealthEvent(
    val causedBy: CanCauseChangeHealthEvent,
    val receivedBy: CanReceiveChangeHealthEvent,
    val amountByWhichToChange: Double,
) {
    interface CanCauseChangeHealthEvent
    interface CanReceiveChangeHealthEvent
}