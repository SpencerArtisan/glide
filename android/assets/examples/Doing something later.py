# If you want to make something happen 1 second later...
timer.after(1000, lambda: alien.x = 100)


# Instead of "lambda", you can give the name of a function
def moveAlien():
    alien.x = 100

timer.after(1000, moveAlien)
