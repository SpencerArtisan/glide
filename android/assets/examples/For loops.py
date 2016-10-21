# if you have an array of 4 baddies, you could loop through them and do something,
# like move them randomly. This goes through the loop three times, with i set to 0,
# then 1, then 2. There is no 3!
for i in range(0, 3):
    baddies[i].x = baddies[i].x + utils.randomIntInRange(-10, 10)
    baddies[i].y = baddies[i].y + utils.randomIntInRange(-10, 10)