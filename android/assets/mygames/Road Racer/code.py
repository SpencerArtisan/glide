# Console Racer - Spencer Ward - 2016          

# Define your variables

screen.backgroundColour = LightGreen
roadLeft = 250
roadWidth = 250
carStartX = roadLeft + roadWidth / 2
score = 0
walls = []

# Define your sprites

car = resources.createImageSprite("car")
car.x = carStartX
car.y = 20
wall = resources.createImageSprite("wall")
wall.x = 999
scoreText = resources.createTextSprite("")
scoreText.setPosition(700, 580)
scoreText.setFontSize(30)
scoreText.setColour(DarkBlue)

# Functions to deal with road and car

def moveRoad():
    global roadLeft
    wallLeft = wall.copy().setPosition(roadLeft, 600)
    wallRight = wall.copy().setPosition(roadLeft + roadWidth, 600)
    wallLeft.runEffect(effects.move(180, 600).withStyle(Linear), AtEndRemoveSprite)
    wallRight.runEffect(effects.move(180, 600).withStyle(Linear), AtEndRemoveSprite)
    walls.append(wallLeft)
    walls.append(wallRight)

    roadLeft = roadLeft + utils.randomInRange(-11, 11)
    if roadLeft < 20:
        roadLeft = 20
    if roadLeft > 720:
        roadLeft = 720

def controlCar():
    if keyboard.Left.isPressed():
        car.x = car.x - 3
    if keyboard.Right.isPressed():
        car.x = car.x + 3

def hitWall():
    return any(wall.overlaps(car) for wall in walls)

def deathAnimation():
    explosion = resources.createImageSprite("explosion")
    explosion.pushToBack()
    explosion.setScale(0.1)
    explosion.setPosition(car.x, car.y)
    explosion.runEffect(effects.sequence(effects.scaleBy(300)))
    scoreText.runEffect(effects.combine(effects.rotateBy(360), effects.scaleBy(4), effects.moveTo(350, 300)))


# Game loop - repeats until you die!

while screen.update():
    moveRoad()
    controlCar()
    scoreText.setText("Score: " + str(score))
    roadWidth = roadWidth - 0.1
    score = score + 1
    if hitWall():
        deathAnimation()
        system.sleep(1800)
        break