# This doubles the width
mySprite.scaleX = 2

# This doubles the height
mySprite.scaleY = 2

# This doubles both
mySprite.setScale(2)

# To make it gradually bigger you could do this in your main game loop
mySprite.scaleX = mySprite.scaleX * 1.1
mySprite.scaleY = mySprite.scaleY * 1.1