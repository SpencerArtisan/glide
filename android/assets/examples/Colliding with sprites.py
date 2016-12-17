if myShip.overlaps(planet):
    resources.createTextSprite("You're dead!")

# This works fine for round and square sprites, but for oblong ones,
# wherever you create your sprite you need to write
myShip.setTargetStyle(Rectangle)


# Instead of using an "overlaps" in your main game loop, you could
# do this before your game loop
alien.runEffect(effects.moveBy(100, 0)).onCollisionWith(ship, die)

def die(alien):
    # Write your death scene here