# x of 0 is the far left, 800 is the far right
car.x = 300

# y of 0 is the bottom, 600 is the top
car.y = 200

# Or move the sprite towards some point by, say, 10 pixels
car.moveTowards(myHouse.x, myHouse.y, 10)

# Or move the sprite towards some some other sprite by, say, 10 pixels
car.moveTowards(myHouse, 10)

# Or move the sprite an an angle, say, 45 degrees by, say, 10 pixels
car.move(10, 45)

# Or to move the sprite slowly to a point, say the center of the screen,
# then remove it when it gets there
car.runEffect(effects.moveTo(400, 300), AtEndRemoveSprite)

# Or to move the sprite slowly at an angle over a distance, say 45 degrees for 200 pixels
car.runEffect(effects.move(45, 200))
