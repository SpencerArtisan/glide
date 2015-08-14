///////////////////////////////////// 
//       Welcome to GLIDE!         // 
//  Start writing your game below  // 
// Look at Samples for inspiration // 
///////////////////////////////////// 

rocket = resources.loadSoundEffect("MLaunch.wav")
music = resources.loadMusic("Mozart.mp3")
volume = 1

console.println "Space = play music"
console.println "Enter = pause music"
console.println "Up = louder"
console.println "Down = quieter"
console.println "S = sound effect"

keyboard.S.onPressed { rocket.play() }
keyboard.Space.onPressed { music.play(volume) }
keyboard.Enter.onPressed { music.pause() }
keyboard.Up.onPressed { 
    volume += 0.2
    music.setVolume(volume) 
}
keyboard.Down.onPressed { 
    volume -= 0.2
    music.setVolume(volume)
}

while(screen.update()) {
}

