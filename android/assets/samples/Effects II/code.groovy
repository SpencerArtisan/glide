destroyEffect = effects.combine(effects.scaleBy(4),
                                effects.transparency(0))

while(screen.update()) {
    // Create a world...
    world = resources.createImageSprite("hello-world.png")
            .setPosition(utils.randomInRange(50, 750), utils.randomInRange(50, 550))
            .setScale(utils.randomInRange(0.1, 0.5))

    // And immediately destroy it. Harsh.
    world.runEffect(destroyEffect, AtEndRemoveSprite)
}
