# Turn it to 90 degrees
mySprite.angle = 90

# Turn it BY 1 degree (do this in a the game loop to make it rotate slowly)
mySprite.rotateBy(1)

# You might find it handy to find the angle between two sprites
mySprite.angleTo(otherSprite)

# Or the angle to some specific point
mySprite.angleTo(someX, someY)
