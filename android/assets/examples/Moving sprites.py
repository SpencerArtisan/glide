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
