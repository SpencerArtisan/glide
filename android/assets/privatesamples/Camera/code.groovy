sprites = []
for(i = 0; i < 8; i++) {
    sprites.add(resources.createImageSprite("hello-world.png")
            .setX(50 + i * 100)
            .setScale(utils.randomInRange(0.1, 0.5))
            .setAngle(utils.randomUpTo(360)))
}

spriteIndex = 0
while(screen.update()) {
    if(!camera.isRunningAnEffect()) {
        alignWithSprite = buildCameraZoomInEffect(sprites[spriteIndex])

        camera.runEffect(alignWithSprite.withDelayBeforeStart(500))
        spriteIndex = (spriteIndex + 1) % 8
    }
}

def buildCameraZoomInEffect(sprite) {
    return effects.combine(
            effects.rotateTo(sprite.angle),
            effects.moveTo(sprite.x, sprite.y),
            effects.zoomTo(1 / sprite.scaleX))
}
