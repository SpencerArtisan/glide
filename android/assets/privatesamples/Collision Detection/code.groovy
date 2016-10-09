system.debugMode(true)

worldWithCircle1 = resources.createImageSprite("hello-world.png")
                                   .setScale(0.5)
                                   .setPosition(200, 500)
                                   .setTargetStyle(Circle)

worldWithCircle2 = resources.createImageSprite("hello-world.png")
                                   .setScale(0.5)
                                   .setPosition(600, 500)
                                   .setTargetStyle(Circle)

worldWithCircle3 = resources.createImageSprite("hello-world.png")
                                   .setScale(0.5)
                                   .setPosition(200, 300)
                                   .setTargetStyle(Circle)

worldWithRectangle1 = resources.createImageSprite("hello-world.png")
                                      .setScale(0.5)
                                      .setAngle(45)
                                      .setPosition(600, 300)
                                      .setTargetStyle(Rectangle)

worldWithRectangle2 = resources.createImageSprite("hello-world.png")
                                      .setScale(0.5)
                                      .setAngle(45)
                                      .setPosition(200, 100)
                                      .setTargetStyle(Rectangle)

worldWithRectangle3 = resources.createImageSprite("hello-world.png")
                                      .setScale(0.5)
                                      .setAngle(70)
                                      .setPosition(600, 100)
                                      .setTargetStyle(Rectangle)

while(true) {

    handleSpritePair(worldWithCircle1, worldWithCircle2)
    handleSpritePair(worldWithCircle3, worldWithRectangle1)
    handleSpritePair(worldWithRectangle2, worldWithRectangle3)
    screen.update()
}

private void handleSpritePair(leftSprite, rightSprite) {

    if(!leftSprite.overlaps(rightSprite)) {
        leftSprite.rotateBy(1)
        rightSprite.rotateBy(-2)
        leftSprite.x += 2
        rightSprite.x -= 2
    }
}
