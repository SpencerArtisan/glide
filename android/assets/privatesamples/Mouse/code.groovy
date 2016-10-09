world = resources.createImageSprite("hello-world.png")

while(screen.update()) {

    world.setPosition(mouse.x(), mouse.y())

    if(mouse.isLeftButtonPressed()) {
        world.angle = -30
    } else if(mouse.isRightButtonPressed()) {
        world.angle = 30
    } else {
        world.angle = 0
    }
}
