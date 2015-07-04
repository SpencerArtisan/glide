///////////////////////////////////////// 
//             Console Race            // 
//                                     // 
//         Spencer Ward (2015)         // 
///////////////////////////////////////// 

screen.backgroundColour = LightGreen
roadLeft = 50
roadWidth = 30
pixelsPerChar = 5.4
previousLefts = []
verticalChars = 66
carStartX = (roadLeft + roadWidth / 2) * pixelsPerChar

car = resources.createImageSprite("car").setPosition(carStartX, 560)
explosion = resources.createImageSprite("explosion").setHidden(true)
scoreText = resources.createTextSprite("0").setPosition(750, 580).setFontSize(20).setColour(DarkBlue)
score = 0

while (screen.update()) {
    moveRoad()  
    controlCar()
   
    if (hitWall()) {
        deathAnimation()
        system.wait(1800)
        system.restart()
    }
    scoreText.setText("Score: " + score++)
}

void moveRoad() {
    roadLeft += utils.randomInRange(-2, 2)
    if (roadLeft < 0) roadLeft = 0
    if (roadLeft > 100) roadLeft = 100
    roadLine = " " * roadLeft + "**" + " " * roadWidth + "**"
    previousLefts.add(roadLeft)
    console.println(roadLine)  
}

void controlCar() {
    if (keyboard.Left.isPressed()) car.x -= 3
    if (keyboard.Right.isPressed()) car.x += 3
}

boolean hitWall() {
    if (previousLefts.size() > verticalChars) {
        carCharX = car.x / pixelsPerChar
        left = previousLefts[previousLefts.size() - verticalChars]
        return carCharX - 1 <= left || carCharX + 3 >= left + roadWidth
    }
    return false
}

void deathAnimation(){
    car.runEffect(effects.rotateBy(5000))
    explosion.setScale(0.1).setPosition(car.x, car.y).setHidden(false)
    explosion.runEffect(effects.sequence(effects.scaleBy(20), effects.scaleTo(0.1)))
}











