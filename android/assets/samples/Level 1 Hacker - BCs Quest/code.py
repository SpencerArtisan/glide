##  BC's Quest for Tyres written by Spencer  October 2016

resources.createImageSprite("grass")
caveman = resources.createImageSprite("caveman")
caveman.y = 200
dinosaur = resources.createImageSprite("dinosaur")
dinosaur.x = 1000
dinosaur.y = 200

while (screen.update()):
    dinosaur.x = dinosaur.x - 7  
    if dinosaur.x < -100:
        dinosaur.x = 900
        
    if keyboard.Left.isPressed():
        caveman.x = caveman.x - 10
    if keyboard.Right.isPressed():
        caveman.x = caveman.x + 10
    
    if caveman.y > 200:
        caveman.y = caveman.y - 7
    else:
        if keyboard.Space.isPressed():
            caveman.y = caveman.y + 260
            
    if caveman.overlaps(dinosaur):
        system.stop()
          
        
    
         
    