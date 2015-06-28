////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//                                          // 
//   Use MOVETOWARDS to move your sprite    // 
////////////////////////////////////////////// 

world = resources.createImageSprite("world.png")
targetX = 400
targetY = 300
speed = 500

while (true) {
    world.moveTowards(targetX, targetY, speed)
    screen.update()
    if (world.x == targetX && world.y == targetY) {
        targetX = utils.randomInRange(150, 750)
        targetY = utils.randomInRange(150, 450)
        speed = utils.randomInRange(100, 1000)
    }
}
sss