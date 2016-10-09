console.println("The quick, brown fox jumped over the lazy dog.")
screen.update()
system.pause(500)

console.println("Pack my box with five dozen liquor jugs")
screen.update()
system.pause(500)

console.println("Amazingly few discotheques provide jukeboxes.")
screen.update()
system.pause(500)

console.println("The five boxing wizards jump quickly.")
screen.update()
system.pause(500)

while(true) {
    console.colour(utils.randomColour())
    console.print("Glide Rules!!! ")
    screen.update()

    if(keyboard.Space.wasJustPressed()) {
        console.clear()
    }
}
