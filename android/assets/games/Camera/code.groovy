////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//      Start writing your game below       // 
// Look in the Game Library for inspiration // 
////////////////////////////////////////////// 

import com.bigcustard.blurp.apimodel.*

effects = new EffectsImpl()
worldImage = blurp.loadImage("world")

sprites = []
for(int i = 0; i < 8; i++) {
    sprites[i] = blurp.createImageSprite(worldImage)
                      .x(50 + i * 100)
                      .scale(utils.random(0.2, 0.5))
                      .rotation(utils.random(360))
}

int spriteIndex = 0
while(true) {
    if(!camera.isRunningEffect()) {
        alignWithSprite = effects.combine(
                    effects.rotateTo(sprites[spriteIndex].rotation),
                    effects.moveTo(sprites[spriteIndex].x, sprites[spriteIndex].y),
                    effects.zoom(1 / sprites[spriteIndex].scaleX))

        camera.runEffect(alignWithSprite.delayBeforeStart(0.5))
        spriteIndex = (spriteIndex + 1) % 8
    }
    blurp.blurpify()
}
