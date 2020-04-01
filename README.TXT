------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------
Ecosystem:

For this exercise, you'll be developing a program that simulates an ecosystem. The user will enter the number of each type of organism (you check that this entry is reasonable), and the program randomly distributes these organisms on 12 by 12 grid (more than one organism can occupy a square). At each turn of the game (as triggered by the use clicking a button), the organisms, which have the properties identified below, check to see whether they will take certain actions, which are also specified below.

Each organism has:

Grass - each unit of grass can produce a new unit of grass every two turns (that is every other turn) - it reproduces in its own square or in an adjacent square (which direction it spreads, including its own square, is random) - it never ages - it only dies through a fire or being eaten - you can have a maximum of ten units of vegetable material (that is, grass and trees combined) on any given square - in the event of a fire, any unit of grass has a 75% chance of dying.

Trees - each tree can produce a new tree every five turns - it reproduces in its own square or in an adjacent square - it never ages - it only dies through a fire - you can have a maximum of ten units of vegetable material (that is, grass and trees combined) on any given square - if a tree wants to spread in a particular direction, it can eliminate the grass that is in that square if necessary to avoid violating the ten vegetable unit limitation - in the event of a fire, any unit of grass has a 60% chance of dying.

Deer - deer can reproduce once every four turns and they must be in perfect health (note, to simplify coding, we've made deer able to asexually reproduce) - deer are not affected by fire  - each deer has health (up to a maximum of eight points - the deer starts with eight) based on eating; each turn that the deer eats (which it does whenever it can), its health goes up by one (until the maximum); if the deer has nothing to eat, its health goes down to eat - during any given move, first the deer should eat, then the deer may move (the deer moves to whatever square adjoining it has the most food, unless the square it is on has the most food); if there is no food in its own square or an adjacent square, the deer will move randomly - deer die once their health reaches zero.

When the program starts up, the position and how long since reproduction must be random (otherwise everything reproduces in lockstep).

Note: Work on this assignment incrementally (get the basics first, if you can get grass to grow, you are at least half way to finishing). If you are stuck, submit what you can do. We will be looking to see that you've demonstrated mastery of the concepts in the book (to demonstrate all the concepts, you need to define all of the species, but you can do this without demonstrating complete mastery of the rules.

I strongly suggest you that you review the Foxes and Rabbits Video after reading Chapter 12 OFWJ. It will provide a good baseline for the programming tasks required for this assignment.
