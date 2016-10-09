NUM_WORLDS = 100

worlds = resources.createImageSprite("hello-world.png")
                  .multiplyBy(NUM_WORLDS)

for(world in worlds) {

    xPos = utils.randomInRange(75, 725)
    yPos = utils.randomInRange(75, 525)

    world.setPosition(xPos, yPos)
         .setScale(utils.randomInRange(0.25, 0.5))
         .rotateBy(utils.randomUpTo(360))
}

while(screen.update()) {
    for(world in worlds) {
        world.rotateBy(world.x % 1 > 0.5 ? 3 : -3)
    }
}
