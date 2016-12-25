##  My Game written by me!  2016
import sys
from com.phidgets import Manager

def AttachHandler(event):
    serialNumber = event.getSource().getSerialNumber()
    name = event.getSource().getDeviceName()
    console.println("Hello Device " + name + ", Serial Number: " + str(serialNumber))

manager = Manager()
manager.addAttachListener(AttachHandler)
print("Opening....")
manager.open()

console.println("Phidget Simple Playground (plug and unplug devices)")
console.println("Press Enter to end anytime...")
while screen.update():
    if keyboard.Enter.isPressed():
        break

console.println("Closing...")
manager.close()


