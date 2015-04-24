////////////////////////////////////////////////// 
//         Welcome to Planet Burpl!             // 
//                                              // 
// Use Arrays to show lots of identical sprites // 
//     Use For to loop through the sprites      //
////////////////////////////////////////////////// 

// Create 4 worlds and spread them out horizontally
worlds = []
for (i in 1..4) {
    world = blurp.imageSprite("world.png")
    world.x = i * 350
    world.y = 900
    worlds.add world
}

// Now move the worlds
while (true) {
    for (world in worlds) {
        world.y -= 10
    }
    blurp.blurpify()
}

