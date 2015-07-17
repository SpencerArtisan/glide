MESSAGE = "The quick, brown fox jumped over the lazy dog."
COLOURFUL_MESSAGE = "All colours can be specified as [Red]red[], [Green]green[] and [Blue]blue[] components"

resources.createTextSprite("LEFT-JUSTIFIED\n" + MESSAGE)
     .setPosition(150, 500)
     .setWrap(220, AlignLeft)

resources.createTextSprite("CENTER-JUSTIFIED\n" + MESSAGE)
     .setPosition(400, 500)
     .setWrap(220, AlignCenter)

resources.createTextSprite("RIGHT-JUSTIFIED\n" + MESSAGE)
    .setPosition(650, 500)
    .setWrap(220, AlignRight)

resources.createTextSprite(COLOURFUL_MESSAGE)
                               .setPosition(400, 400)
                               .setColourTagsEnabled(true)

resources.createImageSprite("hello-world.png")
     .setPosition(250, 200)
     .setTransparency(0.3)

anchorText = resources.createTextSprite("").setPosition(250, 200)

rotateAndScaleText = resources.createTextSprite("Rotate and Scale!").setPosition(600, 200)
rotateAndScaleText.colour = DodgerBlue

frameCount = 0
while(screen.update()) {

    if(frameCount == 0) {
        anchorText.handle = BottomRight
        anchorText.text = "Bottom-Left Handle\nBottom-Left Handle\nBottom-Left Handle"
    } else if(frameCount == 50) {
        anchorText.handle = Bottom
        anchorText.text = "Bottom Handle\nBottom Handle\nBottom Handle"
    } else if(frameCount == 100) {
        anchorText.handle = BottomLeft
        anchorText.text = "Bottom-Right Handle\nBottom-Right Handle\nBottom-Right Handle"
    } else if(frameCount == 150) {
        anchorText.handle = Left
        anchorText.text = "Left Handle\nLeft Handle\nLeft Handle"
    } else if(frameCount == 200) {
        anchorText.handle = TopLeft
        anchorText.text = "Top-Right Handle\nTop-Right Handle\nTop-Right Handle"
    } else if(frameCount == 250) {
        anchorText.handle = Top
        anchorText.text = "Top Handle\nTop Handle\nTop Handle"
    } else if(frameCount == 300) {
        anchorText.handle = TopRight
        anchorText.text = "Top-Left Handle\nTop-Left Handle\nTop-Left Handle"
    } else if(frameCount == 350) {
        anchorText.handle = Right
        anchorText.text = "Right Handle\nRight Handle\nRight Handle"
    } else if(frameCount == 400) {
        anchorText.handle = Center
        anchorText.text = "Center Handle\nCenter Handle\nCenter Handle"
    }
    rotateAndScaleText.rotateBy(2)
    rotateAndScaleText.setScale(utils.waveValue(0.5, 2, 2500))
    frameCount = (frameCount + 1) % 450
    
}
