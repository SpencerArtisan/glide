##  Fruit Machine written by Spencer Ward  -  October 2016

symbols = ["seven", "bell", "melon", "cherries", "lemon"]

console.println("Hold down SPACE to play")

while screen.update():
    # While the SPACE is held down, pick different symbols
    if keyboard.Space.isPressed():
        for column in range(0, 3):
            number = utils.randomIntInRange(0, len(symbols) - 1)
            image = resources.createImageSprite(symbols[number])
            image.x = 200 + 200 * column



