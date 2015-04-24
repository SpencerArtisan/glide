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




//bulets
bullet=blurp.imageSprite("bullet.gif")
bullet.y=-1000
fired= false






f_u_aliens = []
for (i in 1..15) {
    f_u_alien = blurp.imageSprite("f_u_alien.png")
    f_u_alien.x = i * 100
    f_u_alien.y = 900
    f_u_aliens.add f_u_alien
}

//___________________________________________________________________________________________________










//rocket movement
while(true){
   if (keyboard.isKeyPressed(keys.LEFT)&& Rocket.x > 83) Rocket.x -= 8
   if (keyboard.isKeyPressed(keys.RIGHT)&& Rocket.x < 1600) Rocket.x += 8
//__________________________________________________________________________________________________









//bullet shoot
    if  (keyboard.isKeyPressed(keys.SPACE)&& !fired){
        fired=true 
        bullet.y=220
        bullet.x=Rocket.x
    }
//________________________________________________________________________________________________



//bullet fired
    if (fired) {
        bullet.y+=25
        if(bullet.y>1200){
             fired=false
        }
    }
//_________________________________________________________________________________________________


//alien colision 
    if (bullet.x>f_u_alien.x-50 && bullet.x<f_u_alien.x+50){
        if (bullet.y>f_u_alien.y-37 && bullet.y<f_u_alien.y+37){
            f_u_alien.y=2000
        }
    }
//_________________________________________________________________________________________________


//f_u_aliens
    for (f_u_alien in f_u_aliens) {
        f_u_alien.x += 4

 
        if (bullet.x>f_u_alien.x-50 && bullet.x<f_u_alien.x+50){
            if (bullet.y>f_u_alien.y-37 && bullet.y<f_u_alien.y+37){
                f_u_alien.y=2000
            }
        }

        if (f_u_alien.x>1600){
            f_u_alien.y-=25
            f_u_alien.x=83
        }

         
    }
//_________________________________________________________________________________________________
   

     blurp.blurpify()
//_________________________________________________________________________________________________




}

