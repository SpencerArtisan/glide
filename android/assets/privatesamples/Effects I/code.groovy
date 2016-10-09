import com.bigcustard.blurp.model.constants.EffectStyle

resources.createTextSprite("Press Space to Change Page").setPosition(400, 575)

for(x = 0; x < 3; x++) {

    pageX = x * 800
    resources.createTextSprite("Linear")
        .setPosition(250 + pageX, 500)
        .setHandle(Right)
        .setColour(Red)

    linearWorld = resources.createImageSprite("hello-world.png")
                             .setPosition(350 + pageX, 500)
                             .setScale(0.25)
                             .setColour(Red)

    runEffect(linearWorld, Linear, effects)

    for(y = 0; y < 5; y++) {

        style = EffectStyle.values()[(y * 3 + x + 1)]

        yPos = 400 - y * 90
        resources.createTextSprite(style.name())
            .setPosition(pageX + 250, yPos)
            .setHandle(Right)

        effectWorld = resources.createImageSprite("hello-world.png")
                                 .setPosition(pageX + 350, yPos)
                                 .setScale(0.25)
        runEffect(effectWorld, style, effects)
    }
}

currentPage = 0
while(screen.update()) {
    if(keyboard.Space.wasJustPressed()) {
        currentPage = (currentPage + 1) % 3
        moveCamera = effects.moveTo(currentPage * 800 + 400, 300).withDuration(500).withStyle(BounceStop)
        camera.runEffect(moveCamera)
    }
}

private void runEffect(sprite, effectStyle, effects) {

    effect = effects.moveBy(300, 0)
                           .withStyle(effectStyle)
                           .withTimesToRun(1000000)
                           .withYoyoMode(true)
                           .withDelayBeforeStart(500)
                           .withDelayBetweenRuns(500)
    sprite.runEffect(effect)
}
