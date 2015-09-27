////////////////////////////////////////////////////////////////// 
// SQUIRREL EAT SQUIRREL
//              
// Task 1 - Move the squirrels randomly             
// Task 2 - Make a special squirrel for you, it will be stationary
//          and a different colour to the others             
// Task 3 - Move your squirrel with keys             
// Task 4 - If you collide with another squirrel, remove them from
//          the screen and double your size             
// Task 5 - Make the squirrels different sizes             
// Task 6 - If you collide with a bigger squirrel, you die             
// Task 7 - If you collide with a smaller squirrel, change your
//          size by the size of that squirrel             
// Task 8 - If you east all the squirrels, print You Win             
//              
////////////////////////////////////////////////////////////////// 

screen.backgroundColour = Green
squirrels = resources.createImageSprite("squirrel.png").multiplyBy(10)

for (squirrel in squirrels) {
    squirrel.x = utils.randomIntInRange(30, 770)
    squirrel.y = utils.randomIntInRange(30, 570)
}

while(screen.update()) {
    for (squirrel in squirrels) {
        squirrel.x += utils.randomIntInRange(-10, 10)
    }
}
