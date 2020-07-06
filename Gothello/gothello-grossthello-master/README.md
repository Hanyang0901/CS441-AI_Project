# Grossthello: Gothello AI
Bart Massey

This is a simple Java AI for playing
[Gothello](http://pdx-cs-ai.github.io/gothello-project). It
uses depth-limited negamax search.

Build with `javac Grossthello.java`. Run with for example

        java Grossthello white localhost 0 3

where `white` is the side to play, `localhost` is the
machine on which the game server
[gthd](http://pdx-cs-ai.github.com/gothello-gthd) is
running, 0 is the "server number" on that server, and
3 is the negamax search depth.
