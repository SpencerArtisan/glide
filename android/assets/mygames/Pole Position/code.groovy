////////////////////////////////////
//         Pole Position          //
//      Spencer Ward (2015)       //
////////////////////////////////////

SCREEN_DIST = 100
ROAD_BELOW = 150
ROAD_WIDTH = 600
EYE_LEVEL = 300
DEPTH = 1100
TRACK_LENGTH = 18000

resources.createImageSprite("blue").setPosition(0, 1192)
resources.createImageSprite("green").setPosition(0, -608)
 
roads = resources.createImageSprite("road").setX(9999).setTargetStyle(Rectangle).multiplyBy(70)
trees = resources.createImageSprite("bush").setX(9999).multiplyBy(30)
car = resources.createImageSprite("2cv").rotateBy(5).setPosition(300, 70)

roadXs = [0]
carZ = 0
carX = 0

createRoad()
animateCar()

while (screen.update()) {
    drawRoad()
    moveCar()
    if (!roads.any{it.overlaps(car)} && carZ > 0) death()
    carZ += 3
}

void death() {
    car.setImage("explosion")
    system.wait(2000)
    system.restart()
}

void animateCar() {
    car.runEffect(effects.moveBy(0, 4).withTimesToRun(100000).withYoyoMode(true).withDuration(30))
}
 
void moveCar() {
    if (keyboard.Left.isPressed()) {
        carX += 11
        car.setAngle(0)
    } else if (keyboard.Right.isPressed()) {
        carX -= 11  
        car.setAngle(10)
    } else {
        car.setAngle(5)
    }
}

void createRoad() {
    addBend(0, 400)
    while (roadXs.size() < TRACK_LENGTH) {
        addBend((int) utils.randomInRange(-17,17), (int) utils.randomInRange(200, 600))   
    }
}

void drawRoad() {    
    spriteIndex = 0
    treeIndex = 0
    nextTreeZ = 10000
    while (nextTreeZ > carZ + DEPTH) nextTreeZ -= 100
    
    for (roadDist = DEPTH; roadDist > 45; roadDist /= 1.05) {
        roadWidthProj = projectCoord(roadDist, ROAD_WIDTH)
        roadYProj = projectCoord(roadDist, ROAD_BELOW)
        roadXProj = projectCoord(roadDist, carX + roadXs[carZ + (int) roadDist])
        roads[spriteIndex++].setScale(roadWidthProj / 80)
                            .setPosition(400 + roadXProj, EYE_LEVEL - roadYProj)         

        if (carZ + roadDist < nextTreeZ) {
            treeXProj = projectCoord(roadDist, carX + roadXs[carZ + (int) roadDist] - ROAD_WIDTH/2)
            treeXProj2 = projectCoord(roadDist, carX + roadXs[carZ + (int) roadDist] + ROAD_WIDTH/2)
            trees[treeIndex++].setScale(roadWidthProj / 500)
                              .setPosition(400 + treeXProj, EYE_LEVEL - roadYProj/1.5)
            trees[treeIndex++].setScale(roadWidthProj / 500)
                              .setPosition(400 + treeXProj2, EYE_LEVEL - roadYProj/1.5)
                              
            nextTreeZ -= 100
        }
    }
}

void addBend(direction, duration) {
    lastZ = roadXs.size()
    lastX = roadXs[lastZ - 1]
    bendStrength = 0
    for (i = 0; i < duration; i++) {
        bendStrength = 0.5 - Math.abs((i - duration / 2) / duration)
        x = roadXs[roadXs.size() - 1] + direction * bendStrength
        roadXs.add(x)
    }
}

float projectCoord(z, x) {
    return SCREEN_DIST * x / z
}




