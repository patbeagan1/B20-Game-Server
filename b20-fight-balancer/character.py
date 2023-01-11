import math
from functools import reduce


def total(to_reduce):
    return reduce(lambda x, y: x + y, to_reduce)


class Character:
    def __init__(self, name, affects, atk1, atk2, shield):
        self.name = name
        self.affects = affects
        self.health = self.get_max_health()
        self.atk1 = atk1
        self.atk2 = atk2
        self.shield = shield

    def get_str(self):
        return total([f.str for f in self.affects])

    def get_dex(self):
        return total([f.dex for f in self.affects])

    def get_end(self):
        return total([f.end for f in self.affects])

    def get_dur(self):
        return total([f.dur for f in self.affects])

    def get_int(self):
        return total([f.int for f in self.affects])

    def get_cha(self):
        return total([f.cha for f in self.affects])

    def get_will(self):
        return total([f.will for f in self.affects])

    def get_wis(self):
        return total([f.wis for f in self.affects])

    def get_str_mod(self):
        return self.get_mod_of(self.get_str())

    def get_dex_mod(self):
        return self.get_mod_of(self.get_dex())

    def get_end_mod(self):
        return self.get_mod_of(self.get_end())

    def get_dur_mod(self):
        return self.get_mod_of(self.get_dur())

    def get_int_mod(self):
        return self.get_mod_of(self.get_int())

    def get_cha_mod(self):
        return self.get_mod_of(self.get_cha())

    def get_will_mod(self):
        return self.get_mod_of(self.get_will())

    def get_wis_mod(self):
        return self.get_mod_of(self.get_wis())

    @staticmethod
    def get_mod_of(attribute):
        return math.ceil(attribute - 10) // 2

    def get_max_health(self):
        return max(0, math.ceil((self.get_dur_mod() * 2) + (self.get_end_mod() / 2) + 10) * 2) + 1

    def get_max_mana(self):
        return max(0,
                   math.ceil(
                       (
                               (self.get_wis_mod() * 2) + (self.get_will_mod() / 2)
                       ) * 2
                   ) + 4)
