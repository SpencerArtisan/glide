/////////////////////////////////////////////////
//  BUILD THE CLASSIC 1980s GAME - CONNECT FOUR
// 
//              --- PART ONE ---
//
//   Complete the tasks one at a time.  
//   Before you start, read the code and make 
//   sure you understand it.
//   If you get stuck, look at the Glide samples, 
//   or read the extra hints at the bottom of this 
//   file.  
//
//  Task 1 - Stop the token falling when it gets
//           to the bottom
//  Task 2 - Use token.copy() to create another 
//           token at the top once the first token
//           has reached the bottom
//  Task 3 - Press Space to make the second token
//           fall
//  Task 4 - Make the second token stop on top
//           of the first token
//  Task 5 - Create the rest of the tokens and
//           drop them so each lands on top of 
//           the previous ones
//  Task 6 - When you get four in a row, print
//           CONNECT FOUR!
//  Bonus Task - If you have time, make the tokens
//               change from red to yellow then
//               back each turn.
//  
///////////////////////////////////////////////// 

screen.backgroundColour = Beige

// Handy variables
TOP = 556
COLUMN_1_LEFT = 155
tokenSpeed = 0

// Create images
token = resources.createImageSprite("token")
token.setPosition(COLUMN_1_LEFT, TOP)
token.setColour(Red)
resources.createImageSprite("board")

// Main game loop
while(screen.update()) {
    if (keyboard.Space.isPressed()) {
        tokenSpeed = 5        
    }       
    token.y -= tokenSpeed
}



/////////////////////////////////////////////////////////////////
//  Extra hints below.  
//  Don't look unless you're stuck!
//             |
//            \ /
//
// Task 1 - tokenSpeed says how fast it drops.
//          0 means it doesn't drop.  Use an IF
//          to work out when it has gone low 
//          enough to stop falling, then set 
//          tokenSpeed to 0.  Remember you can
//          use Control D when running the game
//          to find screen coordinates.
// Task 2 - You now need to do THREE things when 
//          the first token reaches the bottom - 
//          stop it falling AND copy the token 
//          AND position the new token at the top, 
//          so the code for these things should be 
//          together inside an IF. 
// Task 3 - If you find the first token falling and
//          are baffled, don't panic! The code 
//              token.y -= tokenSpeed 
//          makes the variable called "token" fall,
//          and "token" still refers to the FIRST 
//          token.
//          To make the variable refer to the 
//          SECOND token, where you did the 
//          token.copy(), assign the copied
//          token to the token variable.  
//              token = token.copy()
//          That variable now refers to the SECOND 
//          token!  
// Task 4 - Use a variable to define where to stop 
//          the token falling, then increase that
//          variable by 72 after each token is dropped.       
// Task 5 - You shouldn't have to write any more code
//          to make this work!          
// Task 6 - Use IF to check for four in a row, and
//          resources.createTextSprite to print                   
//          the winning message          
// Bonus Task - After you do token.copy(), write
//              token.setColour(Yellow). To change 
//              it back to Red next time, you need
//              an IF condition to check the token
//              colour.  Use token.colour to get
//              hold of the current token colour.
//                    
/////////////////////////////////////////////////////////////////











