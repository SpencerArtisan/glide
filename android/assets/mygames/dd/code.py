##  Uridium written by Spencer  -  October 2016

SHIP_SPEED = 6
BULLET_SPEED = 13

score = 0
baddieSpeed = 2000

scoreText = resources.createTextSprite("Score: ")
scoreText.x = 700
scoreText.y = 560

ship = resources.createImageSprite("shipdd")
ship.angle = 90

baddie = resources.createImageSprite("baddie")
baddie.angle = 90
baddie.x = 850

bullet = resources.createImageSprite("bullet")
bullet.x = 999

def controlShip():
    if keyboard.Up.isPressed() and ship.y < 560:
        ship.y = ship.y + SHIP_SPEED
    if keyboard.Down.isPressed() and ship.y > 40:
        ship.y = ship.y - SHIP_SPEED
    if keyboard.Left.isPressed() and ship.x > 40:
        ship.x = ship.x - SHIP_SPEED
    if keyboard.Right.isPressed() and ship.x < 760:
        ship.x = ship.x + SHIP_SPEED
    
def addStar():
    star = resources.createImageSprite("dot")
    star.x = 800
    star.y = utils.randomInRange(0, 600)
    speed = utils.randomIntInRange(500, 1500)
    star.setScale((2000 - speed)/1500.0)
    star.runEffect(effects.moveBy(-800, 0).withStyle(Linear).withDuration(speed), AtEndRemoveSprite)

def deathShip():
    explosion = resources.createImageSprite("explosion")
    explosion.x = ship.x
    explosion.y = ship.y
    explosion.runEffect(effects.sequence(effects.scaleBy(10), effects.scaleBy(0.01)))
    text = resources.createTextSprite("GAME OVER!")
    text.fontSize = 40
    ship.hidden = True

def deathBaddie():
    bullet.x = 9999
    explosion = resources.createImageSprite("explosion")
    explosion.x = baddie.x
    explosion.y = baddie.y
    baddie.x = 1000
    baddie.stopEffects()
    explosion.runEffect(effects.scaleBy(3).withDuration(300), baddieDead)

def baddieDead(explosion):
    moveBaddie(baddie)
    explosion.remove()
            
def moveBaddie(baddie): 
    baddie.runEffect(effects.moveTo(utils.randomIntInRange(0, 800), utils.randomIntInRange(0, 600))
          .withDuration(baddieSpeed), moveBaddie)
   
moveBaddie(baddie)

while screen.update():
    controlShip()
    addStar()
    scoreText.text = "Score: " + str(score)
    bullet.x = bullet.x + BULLET_SPEED
    if keyboard.Space.isPressed() and bullet.x > 800:
        bullet.x = ship.x
        bullet.y = ship.y
    if baddie.overlaps(bullet):
        deathBaddie()
        score = score + (2100 - baddieSpeed)
        baddieSpeed = baddieSpeed - 100
    if baddie.overlaps(ship):
        deathShip()
        system.sleep(5000)
        system.stop()
