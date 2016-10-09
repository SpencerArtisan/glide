world = resources.createImageSprite("hello-world.png")

while(screen.update()) {
    world.moveTowards(mouse.x(), mouse.y(), 7.5)
}
