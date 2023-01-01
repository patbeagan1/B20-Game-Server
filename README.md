
`B20Server` is multiplayer game server as an engine. It is a ktor server which can communicate over telnet (a la [MUD]s), or serve a react client.

Since ktor is a multiplatform library, the ultimate goal for the game is to host the server within an Android app. When users would like to play, they will be able to start the server locally on their phone, and other players will be able to interact with it via the locally served react client.

There are a couple of different sources of inspiration for the game, such as:

MUDs

- [Runescape]
- [Aardwolf]

TTRPGs

- [Dungeons&Dragons]
- [Pathfinder]

Roguelikes

- [Nethack]
- [Rogue]

Interactive Fiction

- [Zork]

These are some of the current features:

---

`Look` will scan the current room, and report on people or objects of interest. It gives the following information:

- a description of the current room
- how far away an entity is
- where the exits are
- If the entity is a mob, it will give you an idea of what the mob is trying to do

`Map` displays all of this above information in a graphical format!

<div style="text-align: center;">
<img style="float:right;width: 60%;" src="https://patbeagan.dev/images/b20-look-map.png"/>
<img style="float:right;width: 39%;" src="https://patbeagan.dev/images/b20-map.png">
</div>

---

`Move` works how you might expect in a text-based game. There are 6 directions: north, east, south, west, down and up. Some spaces (like walls) can't be occupied. <br><br>There is a border around the edge of the room that lets you know there is no room in that direction. If you move off of the edge of a room, you will be placed on a tile in the next room.

<div style="text-align: center;">
<img src="https://patbeagan.dev/images/b20-move-room.png" style="width:40%"/>
</div>

`Attack` allows for combat! Using `set`, you can change whether you are making a `melee`,`thrown`, or `ranged` attack.<br><br>Once a character is out of health, they die, and drop their inventory on their current space. If the character is a player, they return to their spawn room.

<div style="text-align: center;">
<img src="https://patbeagan.dev/images/b20-death.png" style="width:40%"/>
</div>

Items can be found on the ground in most rooms. If you `take` an item, it will appear in your `inventory`.<br><br>`Examine` will give you more information about an item on the ground, a mob in the room, or an item in your inventory!

<div style="text-align: center;">
<img src="https://patbeagan.dev/images/b20-take.png" style="width:40%"/>
  <img src="https://patbeagan.dev/images/b20-inventory-examine.png" style="width:40%"/>
</div>

Status `effects` are core to the representation of entities in the game. Each entity has a list of effects that determine what it is, such as its `Ancestry`, any `Level ups`, or `spells` and any temporary statuses which are currently applied to it.`Examining` an entity will give you more information about any statuses may be affecting it at a given time.<br><br>Here, **Bob** is cursed, and he remains cursed for multiple rounds. If he takes any damage during this time, he will die!

<div style="text-align: center;">
<img src="https://patbeagan.dev/images/b20-multiround-spell.png" style="width:40%"/>
</div>

Some actions take `multiple rounds` to complete. A game round occurs once all mobs in the game have taken 2 actions. <br><br>Depending on the type of action you are trying to do, you may need to give up your turn - but you will still have the option of dropping the multiround action to respond to changes in the situation. <br><br>Here, **Alice** walks into a dark room, and needs to cast a `multiple round` spell called `nightsight` in order to increase her visible range. This gives her the nightsight status effect, which lights up the rest of the room, only from her perspective.

<div style="text-align: center;">
<img src="https://patbeagan.dev/images/b20-spells.png" style="width:40%"/><img src="https://patbeagan.dev/images/b20-endspell.png" style="width:40%"/>
</div>

[aardwolf]: https://aardwolf.com
[b20-attack]: https://patbeagan.dev/images/b20-attack.png
[b20-death]: https://patbeagan.dev/images/b20-death.png
[b20-endspell]: https://patbeagan.dev/images/b20-endspell.png
[b20-inventory-examine]: /images/b20-inventory-examine.png
[b20-look-map]: https://patbeagan.dev/images/b20-look-map.png
[b20-map]: https://patbeagan.dev/images/b20-map.png
[b20-move-room]: https://patbeagan.dev/images/b20-move-room.png
[b20-multiround-spell]: https://patbeagan.dev/images/b20-multiround-spell.png
[b20-spells]: https://patbeagan.dev/images/b20-spells.png
[b20-take]: https://patbeagan.dev/images/b20-take.png
[b20-vision]: https://patbeagan.dev/images/b20-vision.png
[d&d]: https://en.wikipedia.org/wiki/Dungeons_%26_Dragons
[dungeons&dragons]: https://en.wikipedia.org/wiki/Dungeons_%26_Dragons
[mud]: https://en.wikipedia.org/wiki/MUD
[mud]: https://en.wikipedia.org/wiki/MUD
[nethack]: https://www.nethack.org/
[pathfinder]: https://en.wikipedia.org/wiki/Pathfinder_Roleplaying_Game
[runescape]: https://www.oldschool.runescape.com
[zork]: https://en.wikipedia.org/wiki/Zork
[rogue]: https://en.wikipedia.org/wiki/Rogue_(video_game)
