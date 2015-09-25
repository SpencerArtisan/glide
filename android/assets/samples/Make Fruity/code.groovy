//////////////////////////////////////// 
// Complete the Fruit Machine Game    // 
// Task 1 - Press Space to stop       //
// Task 2 - Print JACKPOT on match    //
// Task 3 - Press Space to play again //
// Task 4 - Show your money, lose 1   // 
//  credit per go, win 10 for jackpot //
// Task 5 - Win 2 credits for 2 match //
//////////////////////////////////////// 

slot1 = resources.createImageSprite("lemon").setX(250)
slot2 = resources.createImageSprite("horseshoe").setX(400)
slot3 = resources.createImageSprite("redseven").setX(550)

while (screen.update()) {
    sleep(50)

    roll(slot1)
    roll(slot2)
    roll(slot3)
}

void roll(slot) {
    pic = utils.randomIntInRange(1, 3)
    if (pic == 1) slot.image = "lemon"
    if (pic == 2) slot.image = "horseshoe"
    if (pic == 3) slot.image = "seven"
}






















