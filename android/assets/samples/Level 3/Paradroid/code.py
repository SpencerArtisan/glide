##  Paradroid written by Spencer  December 2016

screen.backgroundColour = Maroon

tiles = {'r': "topleft", '-': "horiz", ',': "topright", '|': "vert", 'L': "bottomleft", 'J': "bottomright", ' ': "blank", '*': "empty", '>': "t1", '<': "t2", '@': "hospital"}

rows = ["*****r-------,",
        "r----<     @ >----------------,",
        "|    L-- ----<                L---,",
        "|            |         |          L-----,",
        "L----,       |         |                L----,",
        "*****|  ------,        >---,                 L------,",
        "*****L,       L--------J   L---- -,      r-     | @ >----------,",
        "******L--,           |            |      |      |   |          L------,",
        "*********>- ------,        r-- ------ ----- ----J   L--,              L,",
        "*********|        |        |            |    |         |               |",
        "*********>---- ---<        >--,         |    |                        rJ",
        "******r--J        L- r-- --J  |         |    |         |       r------J",
        "*****rJ              |        L---J   r-J           >----------J",
        "*****|     r------- -J     |          |      r------J",
        "r---->-- --<               >---         r----J",
        "| @  |                    rJ      r-----J",
        "|                         | @ r---J",
        "L----,       r----------------J",
        "*****L-------J"]

BULLET_SPEED = 5
allTiles = []
walls = []
hospitals = []
droids = []
droidBullets = []
droidAngles = {}
droidHealths = {}
droidNumbers = {}
yourNumber = 001

def maxHealth(number):
    return 3 + number / 100

def speed(number):
    return 2 + number / 400
    
def moveDroids():
    for droid in droids:
        if hitWall(droid):
            droidAngles[droid] = droidAngles[droid] + 180
        else:
            if utils.randomIntInRange(0, 20) == 0:
                droidAngles[droid] = utils.randomIntInRange(0, 4) * 90    
            if utils.randomIntInRange(0, (1100 - droidNumbers[droid])/3) == 0:
                fireDroidBullet(droid)
                              
        droid.move(droidAngles[droid], speed(droidNumbers[droid]))  

def fireDroidBullet(droid):
    droidBullet = resources.createImageSprite("bullet").setX(droid.x).setY(droid.y)
    droidBullet.angle = droidBullet.angleTo(you)
    droidBullets.append(droidBullet)    
                    
def moveDroidBullets():
    for droidBullet in droidBullets:
        droidBullet.move(droidBullet.angle, BULLET_SPEED)
        if hitWall(droidBullet):
            droidBullet.remove()
            droidBullets.remove(droidBullet)
            
def checkYouHit():
    global health
    for droidBullet in droidBullets:
        if droidBullet.collidedWith(you):
            droidBullet.remove()
            droidBullets.remove(droidBullet)
            health = health - 1
            if health == 2:
                you.runEffect(effects.transparency(0.3).withDuration(500).withTimesToRun(9999).withYoyoMode(True))           
            if health == 0:
                resources.createTextSprite("GAME OVER").setFontSize(50)
                system.sleep(3000)
                system.restart()
              
def hitWall(what):
    return any(wall.overlaps(what) for wall in walls)  

def inHospital():
    return any(hospital.overlaps(you) for hospital in hospitals)  

def moveYourBullet():
    bullet.move(bullet.angle, BULLET_SPEED)
    if hitWall(bullet):
        bullet.x = 99999
        
def checkDroidsHit():
    global yourNumber
    global yourHealth
    for droid in droids:
        if droid.overlaps(bullet):
            bullet.x = 99999
            droidHealths[droid] = droidHealths[droid] - 1
            if droidHealths[droid] == 2:
                droid.runEffect(effects.transparency(0.3).withDuration(500).withTimesToRun(9999).withYoyoMode(True)) 
            if droidHealths[droid] < 0:
                droid.remove()
                droids.remove(droid)
                if droidNumbers[droid] > yourNumber:
                    yourNumber = droidNumbers[droid]
                    yourHealth = maxHealth(yourNumber)
                    you.setImage(str(yourNumber))
                    you.stopEffects()
                    you.transparency = 1
 
def controlYou():
    if keyboard.Left.isPressed():
        you.x = you.x - speed(yourNumber) 
    if keyboard.Right.isPressed():
        you.x = you.x + speed(yourNumber)       
    if keyboard.Up.isPressed():
        you.y = you.y + speed(yourNumber) 
    if keyboard.Down.isPressed():
        you.y = you.y - speed(yourNumber) 
    if keyboard.Space.isPressed() and (lastX != you.x or lastY != you.y):
        bullet.x = you.x
        bullet.y = you.y
        bullet.angle = bullet.angleTo(lastX, lastY) + 180

def createDroid(number, x, y):
    droid = resources.createImageSprite(str(number)).setColour(Black).setPosition(x, y)
    droids.append(droid)
    droidAngles[droid] = 0
    droidHealths[droid] = maxHealth(number)
    droidNumbers[droid] = number
       
def victory():
    for tile in allTiles:
        tile.runEffect(effects.colour(DarkSeaGreen))
    camera.runEffect(effects.combine(effects.scaleTo(0.21), effects.moveTo(1900, 400)))
    resources.createTextSprite("Ship Cleared").setFontSize(400).setX(1650).setY(800)
    system.sleep(88000)
    system.stop()
          
# Draw ship        
y = 550
for row in rows:
    x = 0
    y = y - 50
    for char in row:
        x = x + 50
        tile = resources.createImageSprite(tiles[char]).setTargetStyle(Rectangle).setPosition(x, y)
        allTiles.append(tile)
        if char != ' ' and char != '@':
            walls.append(tile) 
        if char == '@':
            hospitals.append(tile) 
    
# Create your droid
you = resources.createImageSprite("001").setX(550).setY(450)
bullet = resources.createImageSprite("bullet").setY(9999)
health = maxHealth(yourNumber)

# Create enemy droids
createDroid(224, 150, 350)
createDroid(904, 900, 300)
createDroid(302, 800, -200)
createDroid(711, 1100, 350)
createDroid(629, 1500, 350)
createDroid(224, 1600, 250)
createDroid(476, 1900, -50)
createDroid(629, 2200, 50)
createDroid(302, 2800, 0)
createDroid(224, 500, 200)
createDroid(629, 1100, 0)
createDroid(476, 1800, 0)
createDroid(629, 3000, 100)
createDroid(224, 600, -300)
createDroid(904, 1000, -200)
createDroid(711, 2600, -100)
createDroid(999, 100, -300)

# Main game loop  
while screen.update():
    if hitWall(you):
        you.x = lastX
        you.y = lastY
    else:    
        lastX = you.x
        lastY = you.y

    controlYou()
    moveYourBullet()
    checkDroidsHit()
    moveDroids()
    moveDroidBullets()
    checkYouHit()
    
    if health < maxHealth(yourNumber) and inHospital():
        health = health + 1
        if health == 3:
            you.stopEffects() 
            you.transparency = 1
            
    if len(droids) == 0:
        victory()
                       
    camera.x = you.x
    camera.y = you.y