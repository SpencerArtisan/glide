////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//      Start writing your game below       // 
// Look in the Game Library for inspiration // 
////////////////////////////////////////////// 

import com.bigcustard.blurp.model.constants.*

score=0
text = blurp.createTextSprite("Score: -----").position(20, 440)
blurp.createTextSprite("Press ESCAPE to play again").position(82, 13)

collide= false

kills=0
//star
star=blurp.createImageSprite("Star.gif")

//ufo
ufo=blurp.createImageSprite("ufo")

doublekill=blurp.createImageSprite("Double_kill")

ab=blurp.createImageSprite("bullet")

//Rocket Sprite
Rocket=blurp.createImageSprite("Rocket")

//bulets
bullet=blurp.createImageSprite("bullet")

f_u_aliens = []
for (i in 1..15) {
    f_u_alien = blurp.createImageSprite("f_u_alien")
    f_u_aliens.add f_u_alien
}

//___________________________________________________________________________________________________

//new game loop
while(true) {
    ufo.x=-400
    ufo.y= 450
    doublekill.y=20000
    ab.y=-400
    abfired= false
    Rocket.y=50
    bullet.y=-1000
    fired= false
    score=0


xposition=0
for (f_u_alien in f_u_aliens) {
    f_u_alien.x = xposition
    f_u_alien.y = 400
    xposition=xposition+50
}
    

//main loop

//rocket movement
while(true){
   if (keyboard.isKeyPressed(Key.Key_Left)&& Rocket.x > 33) Rocket.x -= 3
   if (keyboard.isKeyPressed(Key.Key_Right)&& Rocket.x < 760) Rocket.x += 3
//__________________________________________________________________________________________________


//bullet shoot
    if  (keyboard.isKeyPressed(Key.Key_Space)&& !fired){
        fired=true
        bullet.y=90
        bullet.x=Rocket.x
    }

//bullet fired
    if (fired) {
        bullet.y+=10
        if(bullet.y>480){
             fired=false
             kills=0
        }
    }
//_________________________________________________________________________________________________


//f_u_aliens
    for (f_u_alien in f_u_aliens) {
        f_u_alien.x += 4


        if (bullet.x>f_u_alien.x-20 && bullet.x<f_u_alien.x+20){
            if (bullet.y>f_u_alien.y-17 && bullet.y<f_u_alien.y+17){
                kills=kills+1
                if (kills==2){
                    doublekill.y=f_u_alien.y
                    doublekill.x=f_u_alien.x
                    score=score+3

                }
                f_u_alien.y=200000
                score=score+1
                
             }
        }

        if (f_u_alien.x>780){
            f_u_alien.y-=15
            f_u_alien.x=33
        }

         
    }
    doublekill.y+=2

//_________________________________________________________________________________________________

//ab shoot
    if (! abfired){
        abfired=true
        no = (int) utils.random(0,15)
        f_u_alien = f_u_aliens[no]
        if (f_u_alien.y>9000) abfired=false
        ab.y=f_u_alien.y
        ab.x=f_u_alien.x
    }


//ab fired
    if (abfired){
        ab.y-=5
        if (ab.y<0){
            abfired=false
        }
    }

//ab colision
    if (ab.x>Rocket.x-20 && ab.x<Rocket.x+20){
            if (ab.y>Rocket.y-20 && ab.y<Rocket.y+20){
                bang= blurp.createImageSprite("bang.png")
                bang.y=Rocket.y
                bang.x=Rocket.x
                Rocket.y=200000
                blurp.createImageSprite("gameover.png")

               
        }
    }
 

//_________________________________________________________________________________________________

//scoring
    text.colour = Colours.DODGER_BLUE
    
    text.text = "Score: " + score
//_________________________________________________________________________________________________
//ufo movement
    ufo.x+= 5
    if (ufo.x>770){
       ufo.x=-400
    }

//_________________________________________________________________________________________________
//ufo colision
   
    if (bullet.x>ufo.x-20 && bullet.x<ufo.x+20){
        if (bullet.y>ufo.y-17 && bullet.y<ufo.y+17){
            score=score+3
            score=score+1
            collide=true
        }                                             
    }
    if (collide){
        ufo.y-=4
    }
//_________________________________________________________________________________________________
//game restart


    if (keyboard.isKeyJustPressed(Key_Escape)) {
        break
    }
    
//_________________________________________________________________________________________________

     blurp.blurpify()
//_________________________________________________________________________________________________
}
}
                    


