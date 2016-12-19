##  Frogger written by Spencer  December 2016

JUMP = 40
ROADTOP = 270
ROADBOTTOM = ROADTOP - JUMP*6
RIVERTOP = ROADTOP + JUMP*6
BOXLEFT = 60
BOXGAP = 170
dying = False
lifeFrogs = []
for i in range(0, 4):
    lifeFrogs.append(resources.createImageSprite("life").setX(20 + i*20).setY(585))

boxes = [False, False, False, False, False]

hop = resources.loadSoundEffect("hop.wav")
squash = resources.loadSoundEffect("squash.wav")

resources.createImageSprite("river").setScaleX(800).setScaleY(290).setX(0).setY(280)
resources.createImageSprite("grass").setY(537)
resources.createImageSprite("wall").setScaleY(0.95).setY(ROADTOP).copy().setY(ROADTOP - JUMP*6)

log1 = resources.createImageSprite("mediumlog").setX(-120).setY(RIVERTOP - JUMP).setTargetStyle(Rectangle)
turtle1 = resources.createImageSprite("turtle").setX(950).setY(RIVERTOP - JUMP*2).setTargetStyle(Rectangle)
log2 = resources.createImageSprite("largelog").setX(-120).setY(RIVERTOP - JUMP*3).setTargetStyle(Rectangle)
log3 = resources.createImageSprite("smalllog").setX(-120).setY(RIVERTOP - JUMP*4).setTargetStyle(Rectangle)
turtle2 = turtle1.copy().setY(RIVERTOP - JUMP*5).setX(950)

truck = resources.createImageSprite("truck").setX(900).setY(ROADTOP - JUMP).setTargetStyle(Rectangle)
car1 = resources.createImageSprite("car1").setX(-99).setY(ROADTOP - JUMP*2)
car2 = resources.createImageSprite("car2").setX(900).setY(ROADTOP - JUMP*3)
car3 = resources.createImageSprite("car3").setX(-99).setY(ROADTOP - JUMP*4)
car4 = resources.createImageSprite("car4").setX(900).setY(ROADTOP - JUMP*5)

froghome = resources.createImageSprite("home").setY(RIVERTOP + 10).setX(-99)
frog = resources.createImageSprite("frog").setY(ROADBOTTOM).setX(-99)

piggyback = None
piggybackX = 0

def addVehicles():
    addVehicle(truck, -1000, 7500)
    addVehicle(car1, 1000, 3500)
    addVehicle(car2, -1000, 5500)
    addVehicle(car3, 1000, 6200)
    addVehicle(car4, -1000, 6700)
    timer.after(900, addVehicles)

def addVehicle(vehicle, x, travelTime):
    if utils.randomIntInRange(1, 2) == 1:
        vehicle.copy().runEffect(effects.moveBy(x, 0).withDuration(travelTime).withStyle(Linear), AtEndRemoveSprite).onCollisionWith(frog, die)

def addRiverStuff():
    addRiverThing(log1, 1200, 10000)
    addRiverThing(turtle1, -1200, 10000)
    addRiverThing(log2, 1200, 8000)
    addRiverThing(log3, 1200, 12000)    
    addRiverThing(turtle2, -1200, 10000)
    timer.after(1100, addRiverStuff)

def addRiverThing(thing, x, timeToTravel):
    if utils.randomIntInRange(1, 3) == 1:
        thing.copy().runEffect(effects.moveBy(x, 0).withDuration(timeToTravel).withStyle(Linear), AtEndRemoveSprite).onCollisionWith(frog, catchLift)

def die(killer):
    global dying
    global piggyback
    if dying:
        return
    dying = True
    squash.play()
    frog.runEffect(effects.rotateBy(1000))
    lifeFrogs.pop().hidden = True
    if len(lifeFrogs) == 0:
        resources.createTextSprite("GAME OVER").setColour(Red).setY(268)
        system.sleep(3000)
        system.restart()
    else:
        system.sleep(900)
        frog.y = ROADBOTTOM
        dying = False
        piggyback = None

def catchLift(thing):
    global piggyback
    global piggybackX
    piggyback = thing
    piggybackX = frog.x - thing.x

def checkInBox(number):
    x = BOXLEFT + number * BOXGAP
    if frog.x > x - 30 and frog.x < x + 30:
        if boxes[number]:
            die()
        else:
            froghome.copy().setX(x)
            frog.y = ROADBOTTOM
            boxes[number] = True

addVehicles()
addRiverStuff()
system.sleep(2000)
frog.x = 400

while screen.update():
    frog.pullToFront()
 
    if piggyback is not None:
        frog.x = piggyback.x + piggybackX
    if piggyback is None and frog.y > ROADTOP:
        die(None)
   
    if keyboard.Up.wasJustReleased():
        frog.y = frog.y + JUMP
        frog.angle = 0
        hop.play()
        piggyback = None
    if keyboard.Down.wasJustReleased() and frog.y > 90:
        frog.y = frog.y - JUMP
        frog.angle = 180
        hop.play()
        piggyback = None
    if keyboard.Right.wasJustReleased():
        frog.x = frog.x + JUMP
        piggybackX = piggybackX + JUMP
        frog.angle = 90
        hop.play()
        if not frog.overlaps(piggyback):
            piggyback = None
    if keyboard.Left.wasJustReleased():
        frog.x = frog.x - JUMP
        piggybackX = piggybackX - JUMP
        frog.angle = 270
        hop.play()
        if not frog.overlaps(piggyback):
            piggyback = None
                
    if frog.x < 0 or frog.x > 800:
        die(None)
    
    if frog.y >= RIVERTOP:
        checkInBox(0)
        checkInBox(1)
        checkInBox(2)
        checkInBox(3)
        checkInBox(4)
        if boxes.count(True) == 5:
            resources.createTextSprite("You Win!").setY(268).setColour(White)
            system.sleep(5000)
            system.restart()
               
