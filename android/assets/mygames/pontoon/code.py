#####################################
##  PONTOON by Spencer   Oct 2016  ##
#####################################

console.println("Welcome to Pontoon")
console.println("")
console.println("Press T to Twist, S to Stick, R to restart.")

scoreText = resources.createTextSprite("Total: 0")
scoreText.setPosition(550, 580)
scoreText.setFontSize(60)

cardLeft = 100
cardTop = 420
total = 0

def newCard(cardLeft):
    global total
    cardNumber = utils.randomIntInRange(1, 13)
    card = resources.createImageSprite(str(cardNumber))
    card.x = cardLeft
    card.y = cardTop
    total = total + min(10, cardNumber)
    if cardNumber == 1 and total + 10 <= 21:
        total = total + 10

while screen.update():
    system.sleep(200)
    if keyboard.T.isPressed():
        newCard(cardLeft)
        cardLeft = cardLeft + 90
        scoreText.setText("Total: " + str(total))
        if total > 21: 
            resources.createTextSprite("BUST!")
    if keyboard.S.isPressed():
        console.println("Stick on: " + str(total))
        console.println("Next player's turn")
        cardLeft = 100
        cardTop = 260
        total = 0
    if keyboard.R.isPressed():
        system.restart()
        
        



