#####################################
##  PONTOON by Spencer   Oct 2016  ##
#####################################

screen.backgroundColour = DarkGreen

console.println("Welcome to Pontoon")
console.println("")
console.println("Press T to Twist, S to Stick, R to restart.")

# Put the score at the top right
scoreText = resources.createTextSprite("Total: 0")
scoreText.setPosition(650, 500)
scoreText.setFontSize(60)

cardLeft = 100
cardTop = 420
total = 0

while screen.update():
    # 'T' means Twist - Take a new card
    if keyboard.T.isPressed():
        # Pick a random card from 1 (Ace) to 13 (King)
        cardNumber = utils.randomIntInRange(1, 13)
        card = resources.createImageSprite(str(cardNumber))

        # Position the card at (x,y)
        card.x = cardLeft
        cardLeft = cardLeft + 90
        card.y = cardTop

        # Update the hand total
        total = total + min(10, cardNumber)

        # Aces count as 1 or 11. Go for 11, unless it busts you
        if cardNumber == 1 and total + 10 <= 21:
            total = total + 10

        # Show the new score
        scoreText.setText("Total: " + str(total))

        # Check if you've bust
        if total > 21:
            resources.createTextSprite("BUST!")

        system.sleep(200)

    # 'S' means Stick - go to the next player
    if keyboard.S.isPressed():
        console.println("Stick on: " + str(total))
        console.println("Next player's turn")

        # The next card will start on the left and below the previous cards
        cardLeft = 100
        cardTop = cardTop - 150

        # The new player starts with 0
        total = 0

        system.sleep(200)

    if keyboard.R.isPressed():
        system.restart()

