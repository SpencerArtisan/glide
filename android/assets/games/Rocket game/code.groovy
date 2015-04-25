////////////////////////////////////////////// 
//         Welcome to Planet Burpl!         // 
//      Start writing your game below       // 
// Look in the Game Library for inspiration // 
////////////////////////////////////////////// 
//left pocket
import com.bigcustard.blurp.model.constants.*
score=0
text = blurp.createTextSprite("Score: -----").position(80, 1000)




kills=0
//star
star=blurp.createImageSprite("Star.gif")

//ufo
ufo=blurp.createImageSprite("ufo")
ufo.x=-1000
ufo.y= 1000


doublekill=blurp.createImageSprite("Double_kill")
doublekill.y=20000



ab=blurp.createImageSprite("bullet.gif")
ab.y=-1000
abfired= false




//Rocket Sprite
Rocket=blurp.createImageSprite("Rocket.png")
Rocket.y=130




//bulets
bullet=blurp.createImageSprite("bullet.gif")
bullet.y=-1000
fired= false






f_u_aliens = []
for (i in 1..15) {
    f_u_alien = blurp.createImageSprite("f_u_alien.png")
    f_u_alien.x = i * 100
    f_u_alien.y = 900
    f_u_aliens.add f_u_alien
}

//___________________________________________________________________________________________________






//main loop



//rocket movement
while(true){
   if (keyboard.isKeyPressed(Key.Key_Left)&& Rocket.x > 83) Rocket.x -= 8
   if (keyboard.isKeyPressed(Key.Key_Right)&& Rocket.x < 1600) Rocket.x += 8
//__________________________________________________________________________________________________



//bullet shoot
    if  (keyboard.isKeyPressed(Key.Key_Space)&& !fired){
        fired=true
        bullet.y=220
        bullet.x=Rocket.x
    }

//bullet fired
    if (fired) {
        bullet.y+=25
        if(bullet.y>1200){
             fired=false
             kills=0
        }
    }
//_________________________________________________________________________________________________


//f_u_aliens
    for (f_u_alien in f_u_aliens) {
        f_u_alien.x += 10


        if (bullet.x>f_u_alien.x-50 && bullet.x<f_u_alien.x+50){
            if (bullet.y>f_u_alien.y-37 && bullet.y<f_u_alien.y+37){
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

        if (f_u_alien.x>1600){
            f_u_alien.y-=25
            f_u_alien.x=83
        }

         
    }
    doublekill.y+=5

//_________________________________________________________________________________________________

//ab shoot
    if (! abfired){
        abfired=true
        no = (int) utils.random(0,15)
        f_u_alien = f_u_aliens[no]
        if (f_u_alien.y>19000) abfired=false
        ab.y=f_u_alien.y
        ab.x=f_u_alien.x
    }


//ab fired
    if (abfired){
        ab.y-=12
        if (ab.y<0){
            abfired=false
        }
    }

//ab colision
       if (ab.x>Rocket.x-50 && ab.x<Rocket.x+50){
            if (ab.y>Rocket.y-50 && ab.y<Rocket.y+50){
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
    ufo.x+= 12
    if (ufo.x>1600){
       ufo.x=-1000
       ufo.y= 1000
    }

//_________________________________________________________________________________________________
//ufo colision
   

    
 



//_________________________________________________________________________________________________
   

     blurp.blurpify()
//_________________________________________________________________________________________________




}

                    


