///////////////////////////////// 
//           DebugIt 2         // 
///////////////////////////////// 

resources.createImageSprite("grass.png")

GROUND = 210
micky = resources.createImageSprite("pogo.png").setPosition(80, GROUND)
stewie = resources.createImageSprite("tricycle.png").setPosition(1000, GROUND-26)
boing = resources.loadSoundEffect("BOING.mp3")

while (screen.update()) {
    if (keyboard.Space.wasJustPressed()) {
        boing.play()
        micky.y = GROUND + 80
        timer.after(600, { micky.y = GROUND })
    
    stewie.x += 5
    if (stewie.x < 0) stewie.x = 1000
    
    if (stewie.overlaps(stewie)) {
        resources.createImageSprite("smug.png")
        resources.createTextSprite("GAME OVER").setFontSize(120)
        break
    }
}


