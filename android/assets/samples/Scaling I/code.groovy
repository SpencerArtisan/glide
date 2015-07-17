world = resources.createImageSprite("hello-world.png")

while(screen.update()) {
    // Earthquake!
    world.setScale(utils.randomInRange(0.95, 1.05))
}
