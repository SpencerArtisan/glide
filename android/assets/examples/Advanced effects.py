# Effects allow you to move and animate sprites and text

# Here's a simple effect to move a bullet
bullet.runEffect(effects.moveTo(400, 300))

# Here's a slightly more complex one
car.runEffect(effects.moveBy(100, 0).withDuration(9000).withStyle(Linear), AtEndRemoveSprite)

# To make your own effect, first pick a start effect, like moveTo, from this list:
scaleTo(2)        # Makes the sprite 2 times its NORMAL size
scaleBy(1.1)      # Makes the sprite 10% bigger its CURRENT size (good for slowly growing)
scaleTo(2, 3)     # Makes the sprite 2 times and 3 times wider than its NORMAL size
scaleBy(1.1, 1.2) # Makes the sprite 10% wider and 20% taller than its CURRENT size (good for slowly growing)
move(90, 100)     # Moves at 90 degrees by 100 pixels
moveTo(400, 300)  # Move to the centre of the screen
moveBy(10, 5)     # Move 10 pixels to the right and 5 pixels up
rotateTo(45)      # Rotate to a 45 degree angle
rotateBy(10)      # Rotate 10 degrees clockwise
transparency(0.5) # Make it half see through
colour(Red)       # Turn it red

# This effect will happen over a few seconds, and will start slowly, speed up, then slow
# down again. Try it, you'll see what I mean.

# You can make it happen quicker, say in half a second
effects.rotateBy(10).withDuration(500)
# or slower, say in 10 seconds
effects.rotateBy(10).withDuration(10000)

# You can make it happen at a steady speed
effects.rotateBy(10).withStyle(Linear)
# ...or pick from lots of different speed styles...
Linear, SmoothStop, SmoothStart, SmoothStartStop, FastThenSlow, SlowThenFast,
SlowFastSlow, BackStop, BackStart, BackStartStop, BounceStop, BounceStart,
BounceStartStop, ElasticStop, ElasticStart, ElasticStartStop

# You can also run the effect multiple times
effects.rotateBy(10).withTimesToRun(7)

# When running multiple times, you might want the animation to go forwards the backwards
effects.rotateBy(10).withTimesToRun(7).withYoyoMode(True)

# You can add in pauses
effects.rotateBy(10).withDelayBeforeStart(1000)
effects.rotateBy(10).withDelayBetweenRuns(1000)

# You can program effects to happen one AFTER the other with SEQUENCE
car.runEffect(effects.sequence(effects.rotateBy(360), effects.scaleBy(4), effects.moveTo(350, 300)))
# ...or all TOGETHER with COMBINE
car.runEffect(effects.combine(effects.rotateBy(360), effects.scaleBy(4), effects.moveTo(350, 300)))
