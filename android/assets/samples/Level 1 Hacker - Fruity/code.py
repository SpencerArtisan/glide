##  Fruit Machine written by Spencer Ward  -  October 2016

symbols = ["seven", "bell", "melon", "cherries", "lemon"]

console.println("Hold down SPACE to play")
console.println("Control Q to quit")

fruit1 = resources.createImageSprite("seven")
fruit2 = fruit1.copy()
fruit3 = fruit1.copy()
fruit1.x = 200
fruit3.x = 600

while screen.update():
    # While the SPACE is held down, pick different symbols
    if keyboard.Space.isPressed():
        number = utils.randomIntInRange(0, len(symbols) - 1)
        fruit1.setImage(symbols[number])
        number = utils.randomIntInRange(0, len(symbols) - 1)
        fruit2.setImage(symbols[number])
        number = utils.randomIntInRange(0, len(symbols) - 1)
        fruit3.setImage(symbols[number])
