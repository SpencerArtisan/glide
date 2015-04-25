////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//      Start writing your game below       // 
// Look in the Game Library for inspiration // 
////////////////////////////////////////////// 
ufo = blurp.imageSprite("ufo.png")

world = blurp.imageSprite("bullet")
targetX = 400
targetY = 300
speed = 590
world1 = blurp.imageSprite("bullet")
targetX1 = 400
targetY1 = 300
speed1 = 590

while (true) {
 if (keyboard.isKeyPressed(keys.LEFT)&& ufo.x > 83) ufo.x -= 8
   if (keyboard.isKeyPressed(keys.RIGHT)&& ufo.x < 1600) ufo.x += 8
 if (keyboard.isKeyPressed(keys.UP)&& ufo.y < 1000) ufo.y += 8
 if (keyboard.isKeyPressed(keys.DOWN)&& ufo.y > 83) ufo.y -= 8

    world.moveTowards(targetX,targetY,speed) 
   if (world.x == targetX && world.y == targetY) {
        targetX = utils.random(50, 1550)
        
        targetY = utils.random(50, 1000)
        speed = utils.random(100, 1000)
    }
    


 world1.moveTowards(targetX1, targetY1, speed1)
    blurp.blurpify()
    if (world1.x == targetX1 && world1.y == targetY1) {
        targetX1 = utils.random(50, 1550)
        targetY1 = utils.random(50, 1000)
        speed1 = utils.random(100, 1000)
    }
}
      





      


