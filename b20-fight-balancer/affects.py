class Affect:
    def __init__(self, str, dex, end, dur,
                 int, wit, will, wis,
                 aware, spirit, desc):
        self.str = str
        self.dex = dex
        self.end = end
        self.dur = dur
        self.int = int
        self.wit = wit
        self.will = will
        self.wis = wis
        self.aware = aware
        self.spirit = spirit
        self.desc = desc


class Human(Affect):
    def __init__(self):
        super().__init__(
            10, 10, 10, 10,
            10, 10, 10, 10,
            1, 1,
            """Human."""
        )


class Goblin(Affect):
    def __init__(self):
        super().__init__(
            8, 8, 6, 9,
            6, 7, 10, 7,
            1, 1,
            """Goblin."""
        )


class BugBear(Affect):
    def __init__(self):
        super().__init__(
            11, 10, 8, 9,
            7, 7, 10, 7,
            1, 1,
            """Goblin."""
        )


class Golem(Affect):
    def __init__(self):
        super().__init__(
            20, 6, 20, 40,
            3, 3, 20, 3,
            1, 1,
            """Golem."""
        )


class Sickness(Affect):
    def __init__(self):
        super().__init__(
            0, 0, -5, 0,
            0, 0, 0, 0,
            0, 0,
            """Golem."""
        )


class MartialTraining(Affect):
    def __init__(self):
        super().__init__(
            2, 1, 0, 0,
            0, 0, 1, 0,
            0, 0,
            """Martial training."""
        )


class EnhancedClimb(Affect):
    def __init__(self):
        super().__init__(
            0, 1, 1, 0,
            0, 0, 0, 0,
            0, 0,
            """Enhanced climb."""
        )


class Invisibility(Affect):
    def __init__(self):
        super().__init__(
            0, 3, 0, 0,
            0, -1, 0, 0,
            0, 0,
            """Enhanced climb."""
        )


class Exhausted(Affect):
    def __init__(self):
        super().__init__(
            -2, -3, -2, 0,
            0, 0, 0, 0,
            0, 0,
            """Exhaustion."""
        )
