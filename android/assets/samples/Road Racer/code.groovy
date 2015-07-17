/////////////////////////////////////////
//            Console Racer            //
//         Spencer Ward (2015)         //
/////////////////////////////////////////

screen.backgroundColour = LightGreen
roadLeft = 250
roadWidth = 250
carStartX = (roadLeft + roadWidth / 2)
score = 0
walls = []

car = resources.createImageSprite("car").setPosition(carStartX, 20)
explosion = resources.createImageSprite("explosion").setHidden(true)
wall = resources.createImageSprite("wall").setScale(0.2).setX(9999)
scoreText = resources.createTextSprite("").setPosition(750, 580).setFontSize(20).setColour(DarkBlue)

while (screen.update()) {
    moveRoad()
    controlCar()
    if (hitWall()) {
        deathAnimation()
        system.sleep(1800)
        break
    }
    scoreText.setText("Score: " + score++)
    roadWidth -= 0.1
}

void moveRoad() {
    wallLeft = wall.copy().setPosition(roadLeft, 600)
    wallRight = wall.copy().setPosition(roadLeft + roadWidth, 600)
    wallLeft.runEffect(effects.move(180, 600).withStyle(Linear), AtEndRemoveSprite)
    wallRight.runEffect(effects.move(180, 600).withStyle(Linear), AtEndRemoveSprite)
    walls.add(wallLeft)
    walls.add(wallRight)

    roadLeft += utils.randomInRange(-11, 11)
    if (roadLeft < 20) roadLeft = 20
    if (roadLeft > 720) roadLeft = 720
}

void controlCar() {
    if (keyboard.Left.isPressed()) car.x -= 3
    if (keyboard.Right.isPressed()) car.x += 3
}

boolean hitWall() {
    return walls.any{it.overlaps(car)}
}

void deathAnimation(){
    explosion.setScale(0.1).setPosition(car.x, car.y).setHidden(false)
    explosion.runEffect(effects.sequence(effects.scaleBy(300)))
    scoreText.runEffect(effects.combine(effects.rotateBy(360),
            effects.scaleBy(9),
            effects.moveTo(400, 300)))
}