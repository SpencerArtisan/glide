##  My Game written by me!  2016

SLOW_DOWN = 0.99
HURDLE_SLOW_DOWN = 0.9
SPEED_UP = 1

resources.createImageSprite("track").setScale(3).setY(300)

man1 = resources.createAnimationSprite("run", "run5").setX(-1600).setY(345).play(200)
speed1 = 0
jumping1 = False
hurdles1 = []
for i in range(0, 8):
    hurdles1.append(resources.createImageSprite("hurdle").setY(320).setX(-1200 + i*500))

man2 = man1.copy().setY(202).play(200)
speed2 = 0
jumping2 = False
hurdles2 = []
for i in range(0, 8):
    hurdles2.append(resources.createImageSprite("hurdle").setY(176).setX(-1200 + i*500))

def finishJump1():
    global jumping1
    jumping1 = False
    man1.y = 345
    man1.setImages(["run", "run2", "run3", "run4", "run5"])
    
def finishJump2():
    global jumping2
    jumping2 = False
    man2.y = 202
    man2.setImages(["run", "run2", "run3", "run4", "run5"])
    
while screen.update():
    camera.x = (man1.x + man2.x) / 2

    man1.x = man1.x + speed1
    speed1 = speed1 * SLOW_DOWN

    if man1.x > 2600:
        resources.createTextSprite("Player 1 wins!")
        system.sleep(1000)
        system.stop()
        
    if any(hurdle.overlaps(man1) for hurdle in hurdles1):
        speed1 = speed1 * HURDLE_SLOW_DOWN
        
    if keyboard.Z.wasJustReleased():
        speed1 = speed1 + SPEED_UP
    if keyboard.A.wasJustReleased():
        jumping1 = True
        man1.setImages(["jump"])
        man1.y = 390
        timer.after(500, finishJump1)

    man2.x = man2.x + speed2
    speed2 = speed2 * SLOW_DOWN

    if man2.x > 2600:
        resources.createTextSprite("Player 2 wins!")
        system.sleep(1000)
        system.stop()
        
    if any(hurdle.overlaps(man2) for hurdle in hurdles2):
        speed2 = speed2 * HURDLE_SLOW_DOWN
        
    if keyboard.M.wasJustReleased():
        speed2 = speed2 + SPEED_UP
    if keyboard.K.wasJustReleased():
        jumping2 = True
        man2.setImages(["jump"])
        man2.y = 247
        timer.after(500, finishJump2)

        
        
