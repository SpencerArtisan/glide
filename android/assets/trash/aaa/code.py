##  Attack of the Mutant Camels  written by  Spencer Ward   October 2016

background = resources.createImageSprite("landscape")
background.scaleY = 0.3

camel = resources.createImageSprite("camel")
camel.x = 100
camel.y = 250
camelStrength = 100

ship = resources.createImageSprite("ship")

bullet = resources.createImageSprite("bullet")
bullet.x = 10000
bulletSpeed = 0

camelBullet = resources.createImageSprite("bullet")
camelBullet.x = 10000
camelBulletSpeed = 1
camelBulletAngle = 0

camera.zoom = 2


while screen.update():
    camel.x = camel.x + 1
    
    if bullet.overlaps(camel):
        camelStrength = camelStrength - 1
        if camelStrength < 75:
            camel.colour = Orange
        if camelStrength < 50:
            camel.colour = Red
        if camelStrength < 25:
            camel.colour = Purple
        if camelStrength < 0:
            camel.colour = Yellow
            camelStrength = 100
            console.println("Camel killed")
            camel.x = -600  
            camelBulletSpeed = camelBulletSpeed + 1
        
    if camel.x > 1500:
        camel.colour = Yellow
        camelStrength = 100
        camel.x = -600  
        camelBulletSpeed = camelBulletSpeed + 1
      
    if camel.overlaps(ship) or camelBullet.overlaps(ship):
        console.println("GAME OVER!")
        system.stop()
         
    if keyboard.Left.isPressed() and ship.x > -400:
        camera.x = camera.x - 5
        ship.scaleX = -1
        ship.x = ship.x - 6
    if keyboard.Right.isPressed() and ship.x < 1300:
        ship.scaleX = 1
        camera.x = camera.x + 5
        ship.x = ship.x + 6
    if keyboard.Up.isPressed():
        ship.y = ship.y + 4
    if keyboard.Down.isPressed():
        ship.y = ship.y - 4
    if keyboard.Space.isPressed():
        bullet.x = ship.x
        bullet.y = ship.y
        bulletSpeed = ship.scaleX * 10
    if bullet.x < ship.x - 150 or bullet.x > ship.x + 150:
        bullet.x = 10000
    if camelBullet.x < camel.x - 200 or camelBullet.x > camel.x + 200:
        camelBullet.x = camel.x
        camelBullet.y = camel.y
        camelBulletAngle = camel.angleTo(ship) + 90
     
    bullet.x = bullet.x + bulletSpeed
    camelBullet.move(camelBulletAngle, camelBulletSpeed)
    
        
            

    


    


    











