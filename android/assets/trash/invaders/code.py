##  Revenge of the Space Invaders --- written by Spencer --- December 2016

SPEED = 1.003

resources.createImageSprite("hud").setLayer(Background)

alien = resources.createImageSprite("ship")
alien.setScale(0.01)
alien.setColour(LightGreen)

resources.createImageSprite("crosshairs").setLayer(Overlay)

bullet1 = None

while screen.update():
    star = resources.createImageSprite("dot")
    angle = utils.randomInRange(0, 360)
    star.runEffect(effects.move(angle, 600).withDuration(4000).withStyle(SlowThenFast), AtEndRemoveSprite)

    alien.scaleX = alien.scaleX * SPEED
    alien.scaleY = alien.scaleY * SPEED
    alien.x = alien.x + utils.randomInRange(-10, 10)
    alien.y = alien.y + utils.randomInRange(-10, 10)

    if alien.scaleX > 0.3:
        screen.backgroundColour = DarkRed
    if alien.scaleX > 0.8:
        resources.createImageSprite("cracked")
        system.sleep(2000)
        system.restart()

    if keyboard.Left.isPressed():
        camera.angle = camera.angle - 5
    if keyboard.Right.isPressed():
        camera.angle = camera.angle + 5
    if keyboard.Down.isPressed():
        alien.move(camera.angle, 10)
    if keyboard.Up.isPressed():
        alien.move(camera.angle, -10)
    if keyboard.Space.isPressed():
        if bullet1 is not None:
            bullet1.remove()
            bullet2.remove()
        bullet1 = resources.createImageSprite("ball")
        bullet1.x = 0
        bullet1.y = 0
        bullet1.runEffect(effects.moveTo(400, 300), AtEndRemoveSprite)
        bullet2 = bullet1.copy()
        bullet2.x = 800
        bullet2.y = 0
        bullet2.runEffect(effects.moveTo(400, 300), AtEndRemoveSprite)

    if bullet1 is not None and bullet1.x > 390 and bullet1.overlaps(alien):
        alien.setScale(0.01)
        screen.backgroundColour = Black
        SPEED = SPEED * 1.002
