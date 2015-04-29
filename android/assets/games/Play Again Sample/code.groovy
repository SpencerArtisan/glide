////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//      Start writing your game below       // 
//             PLAY AGAIN SAMPLE            // 
////////////////////////////////////////////// 

import com.bigcustard.blurp.model.constants.*

world = blurp.createImageSprite("world.png")
blurp.createTextSprite("Press ESCAPE to play again")


while(true) {
    world.x = 200

    while (true) {
        world.x +=10

        if (keyboard.isKeyJustPressed(Key_Escape)) {
            break
        }     

       blurp.blurpify()
    }

}