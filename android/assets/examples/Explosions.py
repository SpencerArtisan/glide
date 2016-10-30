# A simple explosion - it just gets bigger
explosion = resources.createImageSprite("explosion")
explosion.setScale(0.1)
explosion.runEffect(effects.sequence(effects.scaleBy(300)))

# A complex explosion, replacing your ship with the explosion sprite which shimmers and fades
explosion = resources.createImageSprite("explosion")
shimmer = effects.scaleBy(2).withDuration(50).withTimesToRun(20).withYoyoMode(true)
shimmerAndFade = effects.combine(shimmer, effects.transparency(0).withDuration(1000))
ship.setScale(0.5).setImage("explosion").runEffect(shimmerAndFade)
