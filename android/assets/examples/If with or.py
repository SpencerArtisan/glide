ballSpeed = 10
if ball.x < 0 or ball.x > 800:
    # Ball hit left or right of screen, reverse ball speed
    ballSpeed = -ballSpeed
