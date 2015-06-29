//////////////////////////////////////////////
//         Welcome to Planet Burpl!         //
//      Start writing your game below       //
// Look in the Game Library for inspiration //
//////////////////////////////////////////////

// Constants
BOOST = 0.1
G = 0.01
SHIP_MASS = 1
TOP = 510
RIGHT = 772
BOTTOM = -40
LEFT = 28
BUBBLE_CAPTURE = 10

// Variables
direction = 0

// Images
resources.createImageSprite("bubble").setX(60).setY(233).setTransparency(0.8)
targetBubble = resources.createImageSprite("bubble")
resources.createImageSprite("stars").setScale(1.2).setLayer(Background)
ship = resources.createImageSprite("ship").setTargetStyle(Circle)
planet = resources.createImageSprite("planet").setTargetStyle(Circle).setX(9999)

screen.viewport.setSize(800, 600)

replayLoop:
while (true) {

    level = 1

    mainLoop:
    while (true) {
        showPlanets()
        velocityX = 0
        velocityY = 0
        ship.setX(60).setY(233).setScale(1).setTransparency(1)
        targetBubble.setX(RIGHT - 40).setY(233).setColour(Orange).setScale(1).setTransparency(1)

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
        shrink = effects.scaleTo(0.01).withStyle(SmoothStartStop)
        targetBubble.runEffect(effects.combine(goWhite, shrink))
        ship.runEffect(effects.transparency(0))
        delay(1000)
        level = level + 1
        for (planet in planets) {
            planet.hidden = true
        }
    }

// Death  effect
    shimmer = effects.scaleBy(2).withDuration(50).withTimesToRun(20).withYoyoMode(true)
    shimmerAndFade = effects.combine(shimmer, effects.transparency(0).withDuration(1000))
    ship.setScale(0.5).setImage(resources.loadImage("explosion")).runEffect(shimmerAndFade)
    delay(1000)
    ship.setTransparency(1).setImage(resources.loadImage("ship"))
    for (planet in planets) {
        planet.hidden = true
    }

}

//-----------------------------------------------------------------------

void showPlanets() {
    if (level == 1) {
        planets = planet.setHidden(false).multiplyBy(1)
        planets[0].setX(400).setY(233).setScale(1)
    } else if (level == 2) {
        planets = planet.setHidden(false).multiplyBy(2)
        planets[0].setX(300).setY(363).setScale(1)
        planets[1].setX(600).setY(133).setScale(0.7)
    } else if (level == 3) {
        planets = planet.setHidden(false).multiplyBy(3)

        planets[1].setX(550).setY(373).setScale(0.8)
        planets[2].setX(300).setY(100).setScale(1.1)
        planets[0].setX(150).setY(380).setScale(0.5)
    } else if (level == 4) {
        planets = planet.setHidden(false).multiplyBy(1)
        planets[0].setX(400).setY(-100).setScale(2)
    } else {
        resources.createTextSprite("YOU WIN!").setFontSize(120)
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
    levelText = resources.createTextSprite("LEVEL " + level).setFontSize(80).setX(900).setY(233)
    comeIn = effects.moveTo(400, 233).withStyle(ElasticStop)
    delay = effects.transparency(1).withDuration(300)
    fade = effects.transparency(0).withDuration(1500)
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
    ship.runEffect(effects.rotateBy(amount).withDuration(50).withStyle(SmoothStartStop))
}