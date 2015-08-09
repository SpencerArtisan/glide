world = resources.createImageSprite("hello-world.png")
while (screen.update()) {
    if (keyboard.Space.wasJustPressed()) {
        world.y += 50
        timer.after(1000, {world.y -= 50})
    }
}
















