package com.bigcustard.planet.language;

public class FrameworkKeywords implements Keywords {
    @Override
    public String[] get() {
        return new String[] {
                "AliceBlue", "AntiqueWhite", "Aqua", "Aquamarine", "Azure", "Beige", "Bisque", "Black", "BlanchedAlmond", "Blue",
                "BlueViolet", "Brown", "BurlyWood", "CadetBlue", "Chartreuse", "Chocolate", "Coral", "CornflowerBlue", "CornSilk", "Crimson", "Cyan", "DarkBlue", "DarkCyan",
                "DarkGoldenrod", "DarkGrey", "DarkGray", "DarkSlateGray", "DarkSlateGrey", "DarkGreen", "DarkKhaki", "DarkMagenta", "DarkOliveGreen", "DarkOrange", "DarkOrchid", "DarkRed", "DarkSalmon",
                "DarkSeaGreen", "DarkSlateBlue", "DarkTurquoise", "DarkViolet", "DeepPink", "DeepSkyBlue", "DimGray", "DimGrey", "Gray", "Grey", "DodgerBlue", "FireBrick", "FloralWhite",
                "ForestGreen", "Fuchsia", "Gainsboro", "GhostWhite", "Gold", "Goldenrod", "Green", "GreenYellow", "Honeydew", "HotPink", "IndianRed", "Indigo", "Ivory",
                "Khaki", "Lavender", "LavenderBlush", "LawnGreen", "LemonChiffon", "LightBlue", "LightCoral", "LightCyan", "LightGoldenrodYellow", "LightGray", "LightGrey", "LightSlateGray", "LightSlateGrey",
                "LightGreen", "LightPink", "LightSalmon", "LightSeaGreen", "LightSkyBlue", "LightSteelBlue", "LightYellow", "Lime", "LimeGreen", "Linen", "Magenta", "Maroon", "MediumAquamarine",
                "MediumBlue", "MediumOrchid", "MediumPurple", "MediumSeaGreen", "MediumSlateBlue", "MediumSpringGreen", "MediumTurquoise", "MediumVioletRed", "MidnightBlue", "MintCream", "MistyRose", "Moccasin",
                "NavajoWhite", "Navy", "OldLace", "Olive", "OliveDrab", "Orange", "OrangeRed", "Orchid", "PaleGoldenrod", "PaleGreen", "PaleTurquoise", "PaleVioletRed", "PapayaWhip",
                "PeachPuff", "Peru", "Pink", "Plum", "PowderBlue", "Purple", "Red", "RosyBrown", "RoyalBlue", "SaddleBrown", "Salmon", "SandyBrown", "SeaGreen",
                "Seashell", "Sienna", "Silver", "SkyBlue", "SlateBlue", "SlateGray", "SlateGrey", "Snow", "SpringGreen", "SteelBlue", "Tan", "Teal", "Thistle",
                "Tomato", "Turquoise", "Violet", "Wheat", "White", "WhiteSmoke", "Yellow", "YellowGreen",
                "AtEndRemoveSprite", "transparency", "waveValue", "combine", "Up", "Down", "isPressed", "mouse", "hidden", "timer", "stopwatch", "start", "stop", "reset", "every", "text",
                "wasJustPressed", "wasJustReleased", "wasKeyTyped", "Enter", "typedCharacter", "isRightButtonPressed", "isLeftButtonPressed", "isHolding", "isOver",
                "dragX", "dragY", "wasDragReleased", "wasClicked", "isDragging", "onMouseEnter", "onMouseExit", "SpriteEventHandler", "handle", "onClick", "Runnable",
                "Linear", "SmoothStop", "SmoothStart", "SmoothStartStop", "FastThenSlow", "SlowThenFast", "SlowFastSlow", "BackStop", "BackStart", "BackStartStop", "BounceStop",
                "BounceStart", "BounceStartStop", "ElasticStop", "ElasticStart", "ElasticStartStop",
                "Justification", "AlignLeft", "AlignCenter", "AlignRight", "multiplyBy", "moveTowards",
                "resources", "Image", "loadImage", "Sprite", "createImageSprite", "ImageSprite", "createTextSprite", "handle", "timesToRun", "yoyoMode",
                "Circle", "angle", "camera", "withDelayBeforeStart", "withDelayBetweenRuns", "EffectBase", "EffectGroup", "colour", "Linear", "effects", "EffectStyle", "Effects",
                "isRunningAnEffect", "console", "keyboard", "clear", "fontSize", "Colours", "values", "Right", "Left", "Center",
                "moveTo", "Middle", "TopLeft", "Top", "TopRight", "BottomLeft", "Bottom", "BottomRight", "Space", "system", "pause", "overlaps",
                "position", "println", "Rectangle", "randomInRange", "randomUpTo", "runEffect", "rotateBy", "rotateTo", "scaleX", "scaleY", "scale", "scaleTo",
                "targetStyle", "TargetStyle", "screen", "update", "utils", "x", "y", "wasJustPressed", "zoomTo",
                "elapsedTime", "sequence", "style", "image", "scaleBy", "duration", "layer", "ScreenLayer", "Background", "Main", "Overlay", "onPressed", "onReleased",
                "setX", "setY", "setScaleX", "setScaleY", "setAngle", "setColour", "setTransparency", "setHidden", "setTargetStyle", "setLayer", "setMouse",
                "setPosition", "setScale", "setHeight", "setWidth", "setSize", "setFontSize", "setJustification", "setText", "setWrap", "setWrapWidth", "setImage",
                "setAngle", "setColour", "setPosition", "setZoom", "viewport", "withStyle", "withDuration", "withTimesToRun", "withYoyoMode",
                "Math", "abs", "cos", "sin", "distanceTo", "wait", "remove", "restart", "backgroundColour", "sleep", "moveBy"
        };
    }

    @Override
    public String comment() {
        return null;
    }
}
