////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//      Start writing your game below       // 
// Look in the Game Library for inspiration // 
////////////////////////////////////////////// 

// Constants
BOOST = 0.1
G = 0.01
PLANET_MASS = 100
SHIP_MASS = 1
PLANETS = 1
TOP = 455
RIGHT = 780
BOTTOM = 28
LEFT = 20
BUBBLE_CAPTURE = 10

// Variables
direction = 0

// Images
resources.createImageSprite("bubble").x(60).y(233).transparency(0.8)
targetBubble = resources.createImageSprite("bubble")
resources.createImageSprite("stars").layer(Background)
ship = resources.createImageSprite("ship").targetStyle(Circle)
planet = resources.createImageSprite("planet").targetStyle(Circle).x(9999)

replayLoop:
while (true) {

level = 1

mainLoop:
while (true) {
  showPlanets()
  velocityX = 0
  velocityY = 0
  ship.x(60).y(233).scale(1).transparency(1)
  targetBubble.x(RIGHT - 40).y(233).colour(Orange).scale(1).transparency(1)
    
  levelStart()

  // Wait for key press to start
  while (!keyboard.Space.isPressed()) {
    controlShip()
    screen.update()
  }

  // Main game loop
  levelLoop: 
  while (true) {
    controlShip()
    for (planet in planets) {
        gravity(planet)
        if (planet.overlaps(ship)) {
            break mainLoop
        }
    }
    bounce()
    moveShip()
    if (inTargetBubble()) {
        break levelLoop
    }
    
    screen.update()
  }
  
  // Level complete
  goWhite = effects.colour(White)
  shrink = effects.scaleTo(0.01).style(SmoothStartStop)
  targetBubble.runEffect(effects.combine(goWhite, shrink))
  ship.runEffect(effects.transparency(0))
  delay(1000) 
  level = level + 1
  for (planet in planets) {
    planet.hidden = true
  }
}

// Death  effect
shimmer = effects.scaleBy(2).duration(50).timesToRun(20).yoyoMode(true)
shimmerAndFade = effects.combine(shimmer, effects.transparency(0).duration(1000))
ship.scale(0.5).image(resources.loadImage("explosion")).runEffect(shimmerAndFade)
delay(1000)
ship.transparency(1).image(resources.loadImage("ship"))
for (planet in planets) {
    planet.hidden = true
}

}

//-----------------------------------------------------------------------

void showPlanets() {
    if (level == 1) {
        planets = planet.hidden(false).multiplyBy(1)
        planets[0].x(400).y(233).scale(1)
    } else if (level == 2) {
        planets = planet.hidden(false).multiplyBy(2)
        planets[0].x(300).y(363).scale(1)
        planets[1].x(600).y(133).scale(0.7)
    } else if (level == 3) {
        planets = planet.hidden(false).multiplyBy(3)
        
        planets[1].x(550).y(373).scale(0.8)
        planets[2].x(300).y(100).scale(1.1)
        planets[0].x(150).y(380).scale(0.5)
    } else if (level == 4) {
        planets = planet.hidden(false).multiplyBy(1)
        planets[0].x(400).y(-100).scale(2)
    } else {
        resources.createTextSprite("YOU WIN!").fontSize(120)
        delay(999999)
    }
}

void delay(ms) {
  timer.stopwatch.reset()
  timer.stopwatch.start()
  while (timer.stopwatch.elapsedTime() < ms) {
      screen.update()
  }
}

void levelStart() {
    levelText = resources.createTextSprite("LEVEL " + level).fontSize(80).x(900).y(233)
    comeIn = effects.moveTo(400, 233).style(ElasticStop)
    delay = effects.transparency(1).duration(300)
    fade = effects.transparency(0).duration(1500)
    levelText.runEffect(effects.sequence(comeIn, delay, fade)) 
}

boolean inTargetBubble() {
    return Math.abs(ship.x - targetBubble.x) < BUBBLE_CAPTURE &&
           Math.abs(ship.y - targetBubble.y) < BUBBLE_CAPTURE
}

void bounce() {
    if (ship.x < LEFT) {
        velocityX = Math.abs(velocityX)
    }
    if (ship.x > RIGHT) {
        velocityX = -Math.abs(velocityX)
    }
    if (ship.y < BOTTOM) {
        velocityY = Math.abs(velocityY)
    }
    if (ship.y > TOP) {
        velocityY = -Math.abs(velocityY)
    }
}

void gravity(planet) {
    // F = GMm/r*r and F=m*a where r is distance between bodies
    distance = Math.sqrt((planet.x - ship.x) * (planet.x - ship.x) +
                         (planet.y - ship.y) * (planet.y - ship.y))
    planetMass = planet.scaleX * planet.scaleX * planet.scaleX
    force = G * planetMass * SHIP_MASS / distance * distance
    acceleration = force / SHIP_MASS
    
    // Accelerate in direction of planet
    accelAngle = Math.toDegrees(Math.atan2(ship.y - planet.y, planet.x - ship.x))
    
    accelerateShip(accelAngle, acceleration)
}

void controlShip() {
    if (keyboard.Space.isPressed()) { 
        accelerateShip(ship.angle, BOOST)
    }
    if (keyboard.Left.isPressed()) {
        turnShip(-10)                   
    }
    if (keyboard.Right.isPressed()) {
        turnShip(10)                  
    }
}

void accelerateShip(accelAngle, amount) {
    velocityX = velocityX + amount * utils.cos(accelAngle)
    velocityY = velocityY - amount * utils.sin(accelAngle)
}

void moveShip() {
    ship.x = ship.x + velocityX
    ship.y = ship.y + velocityY
}

void turnShip(amount) {
    ship.runEffect(effects.rotateBy(amount).duration(50).style(SmoothStartStop))
}