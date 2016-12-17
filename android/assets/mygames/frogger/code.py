##  Frogger written by Spencer  December 2016

JUMP = 40
ROADTOP = 270
RIVERTOP = ROADTOP + JUMP*6
BOXLEFT = 60
BOXGAP = 170

boxes = [False, False, False, False, False]

hop = resources.loadSoundEffect("hop.wav")
squash = resources.loadSoundEffect("squash.wav")

resources.createImageSprite("river").setScaleX(800).setScaleY(300).setX(0).setY(280)
resources.createImageSprite("biggrass").setY(550)
resources.createImageSprite("wall").setScaleY(0.95).setY(ROADTOP).copy().setY(ROADTOP - JUMP*5)

log1 = resources.createImageSprite("mediumlog").setX(950).setY(RIVERTOP - JUMP).setTargetStyle(Rectangle)
turtle1 = resources.createImageSprite("turtle").setX(-90).setY(RIVERTOP - JUMP*2)
log2 = resources.createImageSprite("largelog").setX(950).setY(RIVERTOP - JUMP*3).setTargetStyle(Rectangle)
turtle2 = turtle1.copy().setY(RIVERTOP - JUMP*4).setX(-90)
log3 = resources.createImageSprite("smalllog").setX(950).setY(RIVERTOP - JUMP*5).setTargetStyle(Rectangle)

truck = resources.createImageSprite("truck").setX(-99).setY(ROADTOP - JUMP).setTargetStyle(Rectangle)
car1 = resources.createImageSprite("car1").setX(900).setY(ROADTOP - JUMP*2)
car2 = resources.createImageSprite("car2").setX(-99).setY(ROADTOP - JUMP*3)
car3 = resources.createImageSprite("car3").setX(900).setY(ROADTOP - JUMP*4)

froghome = resources.createImageSprite("home").setY(RIVERTOP + 10).setX(-99)
frog = resources.createImageSprite("frog").setY(ROADTOP - JUMP*5).setX(-99)

piggyback = None
piggybackX = 0

    
def addVehicles():
    addVehicle(truck, 1000)
    addVehicle(car1, -1000)
    addVehicle(car2, 1000)
    addVehicle(car3, -1000)
    timer.after(500, addVehicles)

def addVehicle(vehicle, x):
    if utils.randomIntInRange(1, 3) == 1:
        vehicle.copy().runEffect(effects.moveBy(x, 0).withDuration(4000).withStyle(Linear), AtEndRemoveSprite).onCollisionWith(frog, die)

def die(killer):
    squash.play()
    frog.runEffect(effects.rotateBy(1000))
    system.sleep(900)
    system.restart()
  
def addRiverStuff():
    addRiverThing(log1, -1200)
    addRiverThing(turtle1, 1200)
    addRiverThing(log2, -1200)
    addRiverThing(turtle2, 1200)
    addRiverThing(log3, -1200)
    timer.after(900, addRiverStuff)

def addRiverThing(thing, x):
    if utils.randomIntInRange(1, 4) == 1:
        thing.copy().runEffect(effects.moveBy(x, 0).withDuration(9000).withStyle(Linear), AtEndRemoveSprite).onCollisionWith(frog, catchLift)
  
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
            frog.y = ROADTOP - JUMP*5
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
        frog.angle = 90
        hop.play()
    if keyboard.Left.wasJustReleased():
        frog.x = frog.x - JUMP
        frog.angle = 270
        hop.play()
        
    if frog.x < 0 or frog.x > 800:
        die(None)
    
    if frog.y >= RIVERTOP:
        checkInBox(0)
        checkInBox(1)
        checkInBox(2)
        checkInBox(3)
        checkInBox(4)
        if boxes.count(True) == 5:
            resources.createTextSprite("You Win!").setFontSize(50)
            system.sleep(5000)
            system.restart()
                
        
    
 
 

