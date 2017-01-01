# If you want to store a list of things
players = ["erin", "jasper", "peter"]

# Then you can get an item in the list by putting a number in
# square brackets after the list name - 0 is the first item
# This would print 'Hello erin' on the screen
console.println("Hello " + players[0])

# Or you can go through the list items one by one
for player in players:
    console.println("Hello " + player)

# You often want to start with an empty list
aliens = []
# Then add things to it
aliens.append(myBrandNewAlien)
# Sometimes you add several things, so for loops are handy
for i in range(1, 7):
    aliens.append(resources.createImageSprite("alien").setX(i*100))
# You can remove from the end of the list
aliens.pop()
# Remove a specific item from the list
aliens.remove(anAlien)
# And find the length of a list
len(aliens)

# You can check if something is true for all the things in the list
if any(alien.overlaps(myship) for alien in aliens):
    # Ship explodes