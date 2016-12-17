if keyboard.Left.isPressed():
    car.x = car.x - 3
if keyboard.Right.isPressed():
    car.x = car.x + 3

# ...which allows you to press and hold left and right to move the car

# ...but if you don't want press and hold...
if keyboard.Up.wasJustReleased():
    mario.y = mario.y + 10