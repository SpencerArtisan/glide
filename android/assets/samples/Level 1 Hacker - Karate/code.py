##  My Game written by me!  2016

SPEED = 4

resources.createImageSprite("fist_backdrop")
white = resources.createImageSprite("fist_stand")
white.y = 200

yellow = white.copy()
yellow.colour = Yellow
yellow.scaleX = -1
yellow.x = 500

while screen.update():
    if keyboard.Z.isPressed():
        white.x = white.x - SPEED
    if keyboard.X.isPressed():
        white.x = white.x + SPEED
    if keyboard.A.isPressed():
        white.setImage("fist_kick")
        timer.after(200, lambda: white.setImage("fist_stand") )
        if abs(white.x - yellow.x + 35) < 10:
            yellow.setImage("fist_ko")
            break
      
    if keyboard.N.isPressed():
        yellow.x = yellow.x - SPEED
    if keyboard.M.isPressed():
        yellow.x = yellow.x + SPEED
    if keyboard.K.isPressed():
        yellow.setImage("fist_kick")
        timer.after(500, lambda: yellow.setImage("fist_stand") )
        if abs(white.x - yellow.x + 35) < 10:
            white.setImage("fist_ko")
            break









