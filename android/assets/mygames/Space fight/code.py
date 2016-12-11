##  My Game written by me!  2016

screen.backgroundColour = Black
resources.createImageSprite("crosshairs").setLayer(Overlay)

ship = resources.createImageSprite("ship")
ship.setScale(0.01)

while screen.update():
    star = resources.createImageSprite("dot")
    a = utils.randomInRange(0, 360)
    star.runEffect(effects.move(a, 600), AtEndRemoveSprite)

    ship.scaleX = ship.scaleX * 1.01    
    ship.scaleY = ship.scaleY * 1.01    

    ship.x = ship.x + utils.randomInRange(-10, 10)
    ship.y = ship.y + utils.randomInRange(-10, 10)
    
    if keyboard.Left.isPressed():
        camera.x = camera.x - 10
    if keyboard.Right.isPressed():
        camera.x = camera.x + 10
    if keyboard.Down.isPressed():
        camera.y = camera.y - 10
    if keyboard.Up.isPressed():
        camera.y = camera.y + 10

