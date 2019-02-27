# Langtons-Ant
Command-line-based implementation of a modified version of Langton's Ant using Java.

This version adds the following funcitonality to Langton's Ant:
- Custom configurations: The user can define a configuration consisting of left-turns and right-turns the ant will cycle through.
This allows the ant to create different, colorful patterns, depending on the configuaraion specified by the user.
Up to 12 states are supported in a single configuration.
- Grid resizing: The user can, at any point during the execution, resize the grid to a custom size.
- Rewind: The user can rewind steps and continue the execution from there.
- Repositioning the and: At any point during the execution the ant can be deleted and repositoned on the grid.


-- HOW TO USE ---

First you need to crete a new grid, defining a size and a configuration. Then the ant must be placed on the grid.

Now you can take any number of steps, both forward and backwards in time, move the ant, resize the grid and print the grid.

A detailed description of all the commands can be obtained by entering 'help' in the command-line at any time after launch.
