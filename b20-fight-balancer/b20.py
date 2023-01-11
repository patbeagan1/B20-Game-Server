#!/usr/bin/env python3

from random import randint
from random import shuffle
from functools import reduce

from affects import Human, Goblin, MartialTraining, EnhancedClimb, Invisibility, BugBear, Exhausted, Golem, Sickness
from character import Character


def roll(upto):
    return randint(0, upto)


def combat(attacker, defender):
    # Applying damage
    atk_roll = roll(20) + attacker.get_dex_mod()
    def_roll = roll(20) + defender.get_dex_mod()
    damage = 0
    if atk_roll > def_roll:
        weapon_bonus = attacker.atk1 + attacker.atk2 // 2
        strength_bonus = attacker.get_str() // 2
        defender_resist = defender.shield

        damage = weapon_bonus + strength_bonus - defender_resist

        defender.health -= max(0, damage)

    return damage


# Adding in exhaustion
def perform_exhaustion(character):
    end_roll = roll(20) + character.get_end_mod()
    exertion = character.get_str_mod() + character.get_dex_mod()
    if end_roll < exertion:
        character.affects.append(Exhausted())
        print(character.name, "Exhausted", exertion, end_roll)
    else:
        print(character.name, exertion, end_roll)


def print_character(character):
    print(character.name,
          character.get_str(),
          character.get_dex(),
          character.get_end(),
          character.get_dur(),
          character.health)


def get_average_number_of_turns(iterations, counting_encounters):
    results = []

    encounters_til_death = []
    encounters = 0

    team1wins = 0
    team2wins = 0
    team1 = get_team_1()
    for j in range(iterations):
        if not team1:
            team1 = get_team_1()
            encounters_til_death.append(encounters)
            encounters = 1
        else:
            if counting_encounters:
                team1 = get_team_1()
            encounters += 1

        team2 = get_team_2()

        def random_from(team):
            index = roll(len(team)) - 1
            return team[index]

        def team_health(team):
            if not team:
                return 0
            return reduce(
                lambda x, y: x + y, [f.health for f in team])

        turn_count = 0
        for j in team1 + team2:
            print(j.name, j.get_str(), j.get_dex(), j.get_end(), j.get_dur(), j.get_max_health(), j.health, sep=' \t')
        print(turn_count, len(team1), len(team2), team_health(team1), team_health(team2), sep='   \t')

        while True:
            turn_count += 1
            roster = team1 + team2
            shuffle(roster)

            for attacker in roster:
                if attacker in team1:
                    if not team2:
                        break
                    defender = random_from(team2)
                elif attacker in team2:
                    if not team1:
                        break
                    defender = random_from(team1)
                else:
                    continue
                damage = combat(attacker, defender)

                print(attacker.name, ">", defender.name, '\t', int(round(damage)))

                team1 = [i for i in team1 if i.health > 0]
                team2 = [i for i in team2 if i.health > 0]

            print(turn_count,
                  "t1count " + str(len(team1)),
                  "t2count " + str(len(team2)),
                  "t1health " + str(team_health(team1)),
                  "t2health " + str(team_health(team2)),
                  sep=' ')
            results.append(turn_count)
            if team_health(team1) <= 0:
                print("Team2 win")
                print()
                print()
                print()
                print()
                print("_____________________________")
                team2wins += 1
                break
            elif team_health(team2) <= 0:
                print("Team1 win")
                print("|")
                print("|")
                team1wins += 1
                for c in team1:
                    perform_exhaustion(c)
                break

    print()
    print("Avg num turns:", round(sum(results) / len(results), 2))
    print("Team 1 wins:", team1wins)
    print("Team 2 wins:", team2wins)
    print("Team1 percent wins:", round(team1wins / (team1wins + team2wins), 2))
    print(encounters_til_death)
    print("Avg encounters til death:", round(sum(encounters_til_death) / len(encounters_til_death), 2))


def get_team_1():
    return [
        Character("hum1", [
            Human()
        ], 5, 2, 4),
        Character("hum2", [
            Human(),
        ], 3, 0, 2)
    ]


def get_team_2():
    return [
        Character("gob1", [
            Goblin(),
        ], 3, 0, 1),
        Character("gob2", [
            Goblin(),
        ], 3, 0, 1),
        Character("gob3", [
            Goblin(),
        ], 3, 0, 1)
    ]


get_average_number_of_turns(3000, True)
