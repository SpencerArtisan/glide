//////////////////////////////////////////////
//                  ASTRA                   //
//           A space gravity game           //
//          by Spencer Ward (2015)          //
//////////////////////////////////////////////

screen.viewport.setSize(800, 600)

// Constants
BOOST = 0.1
G = 0.01
SHIP_MASS = 1
TOP = 572
RIGHT = 772
BOTTOM = 28
LEFT = 28
BUBBLE_CAPTURE = 15

// Sprites
resources.createImageSprite("stars").setScale(1.2).setLayer(Background)
resources.createImageSprite("bubble").setPosition(60, 300).setTransparency(0.8)
targetBubble = resources.createImageSprite("bubble")
ship = resources.createImageSprite("ship")
planets = []

replayLoop: 
while (true) {
    level = 1

    mainLoop:
    while (true) {
        showPlanets()
        velocityX = 0
        velocityY = 0
        ship.setPosition(60, 300).setScale(1).setTransparency(1)
        targetBubble.setPosition(732, 300).setColour(Orange).setScale(1).setTransparency(1)

        levelStart()

        // Wait for key press to start
        while (!keyboard.Space.isPressed()) {
            controlShip()
            screen.update()
        }

        // Main game loop
        levelLoop:
        while (screen.update()) {
            controlShip()
            for (planet in planets) {
                gravity(planet)
                if (planet.overlaps(ship)) {
                    break mainLoop
                }
            }
            bounce()
            moveShip()
            if (ship.distanceTo(targetBubble) < BUBBLE_CAPTURE) {
                break levelLoop
            }
        }

        // Level complete
        goWhite = effects.colour(White)
        shrink = effects.scaleTo(0)
        targetBubble.runEffect(effects.combine(goWhite, shrink))
        ship.runEffect(effects.transparency(0))

        system.sleep(1500)
        level = level + 1
    }

// Death  effect
    shimmer = effects.scaleBy(2).withDuration(50).withTimesToRun(20).withYoyoMode(true)
    shimmerAndFade = effects.combine(shimmer, effects.transparency(0).withDuration(1000))
    ship.setScale(0.5).setImage("explosion").runEffect(shimmerAndFade)
    system.sleep(1500)
    ship.setTransparency(1).setImage("ship")
}

//-----------------------------------------------------------------------

void showPlanets() {
    planets.each { it.remove() }
    planet = resources.createImageSprite("planet").setX(9999)

    if (level == 1) {
        planets = [planet]
        planets[0].setPosition(400,300)
    } else if (level == 2) {
        planets = planet.multiplyBy(2)
        planets[0].setPosition(300, 423)
        planets[1].setPosition(600, 153).setScale(0.7)
    } else if (level == 3) {
        planets = planet.multiplyBy(3)
        planets[1].setPosition(550, 423).setScale(0.8)
        planets[2].setPosition(300, 130).setScale(1.1)
        planets[0].setPosition(150, 470).setScale(0.5)
    } else if (level == 4) {
        planets = [planet]
        planets[0].setPosition(400, -100).setScale(2)
    } else {
        resources.createTextSprite("YOU WIN!").setFontSize(120)
        while(screen.update()) {
            if(keyboard.Space.wasJustPressed()) {
                system.restart()
            }
        }
    }
}

void levelStart() {
    levelText = resources.createTextSprite("LEVEL " + level).setFontSize(80).setPosition(900, 300)
    comeIn = effects.moveTo(400, 300).withStyle(ElasticStop)
    fade = effects.transparency(0).withDuration(1500)
    levelText.runEffect(effects.sequence(comeIn, fade.withDelayBeforeStart(300)))
}

void bounce() {
    if (ship.x < LEFT) {
        velocityX = Math.abs(velocityX)
    } else if (ship.x > RIGHT) {
        velocityX = -Math.abs(velocityX)
    }

    if (ship.y < BOTTOM) {
        velocityY = Math.abs(velocityY)
    } else if (ship.y > TOP) {
        velocityY = -Math.abs(velocityY)
    }
}

void gravity(planet) {
    // F = GMm/r*r and F=m*a where r is distance between bodies
    distance = planet.distanceTo(ship)
    planetMass = planet.scaleX * planet.scaleX * planet.scaleX
    force = G * planetMass * SHIP_MASS / distance * distance
    acceleration = force / SHIP_MASS

    // Accelerate in direction of planet
    accelAngle = ship.angleTo(planet)
    accelerateShip(accelAngle, acceleration)
}

void controlShip() {
    if (keyboard.Space.isPressed()) {
        accelerateShip(ship.angle, BOOST)
        ship.setImage("shipwithflames")
    } else {
        ship.setImage("ship")
    }
    if (keyboard.Left.isPressed()) {
        ship.angle -= 5
    }
    if (keyboard.Right.isPressed()) {
        ship.angle += 5
    }
}

void accelerateShip(accelAngle, amount) {
    velocityX += amount * utils.cos(accelAngle)
    velocityY -= amount * utils.sin(accelAngle)
}

void moveShip() {
    ship.x += velocityX
    ship.y += velocityY
}