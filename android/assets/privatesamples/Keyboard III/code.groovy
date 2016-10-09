keyboard.Enter.onPressed({ console.println("Enter Down") })

keyboard.Enter.onReleased({ console.println("Enter Up") })

while(screen.update()) {
    if(keyboard.Space.wasJustPressed()) {
        console.println("Space Down")
    }
    if(keyboard.Space.wasJustReleased()) {
        console.println("Space Up")
    }
}
