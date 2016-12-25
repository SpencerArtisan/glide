# To create a normal sprite
car = resources.createImageSprite("image-name")

# You can change the way a sprite looks
car.setImage("another-image")

# Or you can make sprites which animate using several images
# Don't forget to PLAY the animation. The 500 means wait half a second between
# animation frames
car = resources.createAnimationSprite("image1", "image2", "image3")
car.play(500)

# To change the way an animated sprite looks
car.setImages(["image4", "image5"])
