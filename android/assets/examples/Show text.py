# Or for text you can size and change...
scoreText = resources.createTextSprite("Score: 0")
scoreText.x = 700
scoreText.y = 300
scoreText.fontSize = 30
scoreText.colour = DarkBlue
scoreText.text = "Score: 0"
myScore = 999

# Then in your main game loop, every time the score changes you need to update the text
scoreText.text = "Score: " + str(myScore)
