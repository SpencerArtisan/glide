world = resources.createImageSprite("hello-world.png")

timer.every(500, { world.hidden = !world.hidden })
while(screen.update()) {
}
