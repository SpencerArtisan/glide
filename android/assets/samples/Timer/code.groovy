world = resources.createImageSprite("hello-world.png")
timer.every(1000, { world.runEffect(effects.rotateBy(6).withDuration(500).withStyle(BounceStop)) })

while(screen.update()) {
}
