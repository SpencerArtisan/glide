///////////////////////////////////// 
//       Welcome to GLIDE!         // 
//  Start writing your game below  // 
// Look at Samples for inspiration // 
///////////////////////////////////// 


slot1 = resources.createImageSprite("lemon").setX(250)
slot2 = resources.createImageSprite("horseshoe").setX(400)
slot3 = resources.createImageSprite("seven").setX(550)
score = 20

void mainRoll(){
    while (screen.update()) {
        system.sleep(50)

        roll(slot1)
        roll(slot2)
        roll(slot3)
        if (keyboard.Space.isPressed()) {
            if (slot1.image == slot2.image) {
                if (slot2.image == slot3.image) {
                    resources.createTextSprite("Jackpot").setColour(Red).setScale(3)
                    score += 10
                }
            }
        break
        }
    }
}

void roll(slot) {
    pic = utils.randomIntInRange(1, 3)
    if (pic == 1) slot.image = "lemon"
    if (pic == 2) slot.image = "horseshoe"
    if (pic == 3) slot.image = "seven"
}

mainRoll()
while(true) {
    resources.createTextSprite("Score: " + score).setColour(White).setX(750).setY(550)
    system.sleep(1000)
    if(keyboard.Space.isPressed()) {
        mainRoll()
        score -= 1
    }    
}
