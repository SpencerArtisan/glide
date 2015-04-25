////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//      Start writing your game below       // 
// Look in the Game Library for inspiration // 
////////////////////////////////////////////// 
import com.bigcustard.blurp.model.constants.Colours

text = blurp.createTextSprite("Score: -----").position(80, 1000)
text.colour = Colours.DODGER_BLUE
score = 1000

while (true) {
    score = score + 1
    text.text = "Score: " + score    
    blurp.blurpify()
}