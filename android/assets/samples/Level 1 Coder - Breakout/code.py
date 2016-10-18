##  BREAKOUT by Spencer Ward  - October 2016      

bat = resources.createImageSprite("bat")
bat.targetStyle = Rectangle         # We use overlap later on, so you have to tell Glide the bat shape
bat.y = 60

ball = resources.createImageSprite("ball")
speedX = 6
speedY = 6

while screen.update():
    ball.x = ball.x + speedX
    ball.y = ball.y + speedY
    
    if keyboard.Left.isPressed():
        bat.x = bat.x - 6
    if keyboard.Right.isPressed():
        bat.x = bat.x + 6
    if ball.x < 0 or ball.x > 800:    # if the ball hits the right or left wall, bounce
        speedX = -speedX             
    if ball.y > 600:                  # if the ball hits the top, bounce
        speedY = -speedY            
    if ball.overlaps(bat):            # if the ball hits the bat, bounce
        speedY = abs(speedY)          # abs makes numbers positive, so the ball will move UP 
    if ball.y < 0:                    # if the ball goes off the bottom, game over!
        system.stop()



