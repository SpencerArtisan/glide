///////////////////////////////////// 
//            DebugIt 1            // 
//  Can you get the game working?  // 
///////////////////////////////////// 

resources.createImageSprite("pool").setPosition(200, 370)
player1 = resource.createImageSprite("swimmer").setPosition(630, 44).setColour(Pink)
player2 = resource.createImageSprite("swimmer").setPosition(680, 120).setScale(0.8)

if (keyboard.Z.wasJustPressed()) player1.moveTowards(22, 245, 20)
if (keyboard.M.wasJustPressed()) player2.moveTowards(110, 250, 18)

while (screen.update()) {
}