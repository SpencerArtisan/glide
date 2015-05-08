////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//      Start writing your game below       // 
// Look in the Game Library for inspiration // 
////////////////////////////////////////////// 

text = blurp.createTextSprite("Score: -----").position(80, 440)
text.colour = DodgerBlue
score = 1000

while (true) {
    score = score + 1
    text.text = "Score: " + score    
    blurp.blurpify()
}




