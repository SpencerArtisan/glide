////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//      Start writing your game below       // 
// Look in the Game Library for inspiration // 
////////////////////////////////////////////// 
//star
star=blurp.imageSprite("Star.gif")

//Rocket Sprite
Rocket=blurp.imageSprite("Rocket.png")
Rocket.y=130

//f_u_alien Sprite
f_u_alien=blurp.imageSprite("f_u_alien.png")
f_u_alien.y=1000
f_u_alien.x=80

//f_u_aliens


//alien movement
movement=8

//bulets
bullet=blurp.imageSprite("bullet.gif")
bullet.y=-1000
fired= false

//___________________________________________________________________________________________________










//rocket movement
while(true){
 if (keyboard.isKeyPressed(keys.LEFT)&& Rocket.x > 83) Rocket.x -= 8
 if (keyboard.isKeyPressed(keys.RIGHT)&& Rocket.x < 1600) Rocket.x += 8

//bullet shoot
if (keyboard.isKeyPressed(keys.SPACE)&& !fired){
    fired=true 
    bullet.y=220
    bullet.x=Rocket.x
}
if (fired) {
    bullet.y+=25
    if(bullet.y>1200){
       fired=false
    }
}

//alien move
f_u_alien.x+=5
if (f_u_alien.x>1600){
    f_u_alien.y-=25
    f_u_alien.x=83
}

//alien colision 
 if (bullet.x>f_u_alien.x-50 && bullet.x<f_u_alien.x+50){
if (bullet.y>f_u_alien.y-37 && bullet.y<f_u_alien.y+37){
   f_u_alien.y=2000
}}
  blurp.blurpify()
}

















