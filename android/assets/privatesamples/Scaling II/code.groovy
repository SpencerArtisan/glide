world = resources.createImageSprite("hello-world.png")

while(screen.update()) {
    waveValue = utils.waveValue(0, 0.5, 1000)
    world.setScaleX(0.75 + waveValue)
    world.setScaleY(1.25 - waveValue)
}
