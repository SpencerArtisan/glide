# Variables can useful for things like scores, health or lives.
# Score generally start at zero
score = 0

# Scores get increased as you do stuff
score = score + 1

# Sometimes you want a variable, but you won't know its value until
# later on in the game
myVariable = None

# Then you can check in your game loop if it has a value yet
if myVariable is not None:
    # Do something with your variable


# If you're using functions, you may hit a problem where changing a
# variable inside a function doesn't work.  The trick is to use "global"...
myVariable = 10

def doStuff():
    global myVariable
    myVariable = myVariable + 1
