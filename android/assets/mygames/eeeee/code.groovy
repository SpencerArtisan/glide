effects.setDefaultDuration(500)

squish = effects.scaleTo(1, 0.7)
goRed = effects.colour(Red)
squishAndGoRed = effects.combine(squish, goRed)

spin = effects.rotateBy(720)
normalise = effects.scaleTo(1)
spinAndNormalise = effects.combine(spin, normalise)

animation = effects.sequence(squishAndGoRed, spinAndNormalise)
animation = animation.withTimesToRun(4).withYoyoMode(true).withDelayBetweenRuns(250)

world1 = resources.createImageSprite("hello-world.png").setPosition(250, 300)
world2 = resources.createImageSprite("hello-world.png").setPosition(550, 300)

world1.runEffect(animation)
world2.runEffect(animation.withDelayBeforeStart(1000))

while(screen.update()) {
}


 