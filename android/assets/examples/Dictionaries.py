# Dictionaries are used to link from one thing to another
# For example, say you have some alien sprites and you want to
# give them different speeds.  You might use a list for the alien sprites...
aliens = [alienSprite1, alienSprite2, alienSprite3]
# Then use a dictionary for linking aliens to speeds
alienSpeeds = {alienSprite1: 3, alienSprite2: 8, alienSprite3: 4}
# Later in your game, say you have the alien sprite and want to find
# its speed...
speed = alienSpeeds[anAlien]
# Or you could change the speeds in the dictionary
alienSpeeds[anAlien] = newSpeed

# You can start with an empty dictionary and add entries to it
alienSpeeds = {}
alienSpeeds[anAlien] = 3
