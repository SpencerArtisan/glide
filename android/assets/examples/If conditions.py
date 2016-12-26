if myCar.x < 0:
    # Car hit the left of the screen, do something about it!
elif myCar.x > 800:
    # Car hit the right of the screen, do something else!
else:
    # Car has not hit either side of the screen, you may
    # or may not need to do something

# When checking if something is equal, you use ==
if myScore == 100:
    # Go to level 2

# To check if something is NOT true
if not myCar.overlaps(road):
    # Car has come off the road, so do something...

# To check if two things are true
if bullet.overlaps(ship) and shieldsDown:
    # If the bullet hits the ship when the shields are down, you're dead!

# To check if at least one of two things is true
if ball.x < 0 or ball.x > 800:
    # Ball hit left or right of screen, so bounce
