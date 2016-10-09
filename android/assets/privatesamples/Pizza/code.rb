$screen.viewport.set_size 800, 600
$resources.create_image_sprite("gingham.png").set_layer(Background).set_colour(Gainsboro)
$resources.load_music("music.mp3").play(0.5)

@fly_spawn_rate = 2000
@score = 0
@swat_count = 0

@score_sprite = $resources.create_text_sprite("SCORE 000000").set_position(150, 570).set_font_size(50).set_layer(Overlay)
@pizza = $resources.create_image_sprite "pizza.png"
@squish_sound = $resources.load_sound_effect "squish.wav"
@chomp_sound = $resources.load_sound_effect "chomp.wav"

@flies = [];

Fly_To_Pizza = $effects.combine $effects.move_to(400, 300).with_style(Linear).with_duration(10000),
                                $effects.scaleTo(0.25).with_style(Smooth_Stop).with_duration(10000)

Fly_Swatted = $effects.move_by(0, -800)

Fly_Away = $effects.combine $effects.move_by(0, 600), $effects.rotate_to(0), $effects.transparency(0)

Victory_Dance = $effects.combine $effects.scaleTo(2),
                                 $effects.moveTo(400, 300),
                                 $effects.rotateBy(360)

Score_Animation = $effects.move_by(0, 50).with_style(Fast_Then_Slow)

Message_Show = $effects.move_to(400, 300).with_style(Bounce_Stop)

Message_Hide = $effects.move_to(400, -50).with_style(Slow_Then_Fast)

Message_Show_And_Hide = $effects.sequence Message_Show, Message_Hide

End_Game_Message = $effects.combine $effects.rotate_by(-720),
                                    $effects.transparency(1),
                                    $effects.scale_to(1)

def spawn_fly
    new_fly = $resources.create_animation_sprite "fly0.png", "fly1.png"
    direction = $utils.random_up_to 360
    new_fly.set_angle(direction)
       .move(direction + 180, 400)
       .play(50)
    new_fly.run_effect Fly_To_Pizza

    new_fly.on_collision_with(@pizza) { |fly| reached_pizza fly }
    new_fly.on_mouse_pressed { |fly| killed fly }
    @flies << new_fly

    $timer.after(@fly_spawn_rate) { spawn_fly }
end

def reached_pizza fly
    $timer.remove_all
    fly.on_collision_with @pizza, Do_Nothing
    fly.on_mouse_pressed Do_Nothing

    @flies.each { |other_fly|
        other_fly.run_effect(Fly_Away, At_End_Remove_Sprite) if other_fly != fly
    }

    @chomp_sound.play
    @pizza.run_effect $effects.scaleTo 0
    fly.run_effect Victory_Dance
    end_game "GAME OVER"
end

def killed fly
    fly.on_collision_with @pizza, Do_Nothing
    fly.run_effect(Fly_Swatted, At_End_Remove_Sprite)

    @squish_sound.play()
    distance = @pizza.distance_to(fly)
    if distance < 130
        increase_score fly, 1000, Lime
    elsif distance < 200
        increase_score fly, 500, Orange
    else
        increase_score fly, 100, Red
    end
    @flies.delete(fly)
end

def increase_score(fly, score, colour)
    $resources.create_text_sprite("+%d" % score)
        .set_position(fly.x, fly.y)
        .set_font_size(50)
        .set_colour(colour)
        .run_effect(Score_Animation, At_End_Remove_Sprite)
    @score += score
    @score_sprite.text = "SCORE %06d" % @score
    handle_level
end

def handle_level
    @swat_count += 1
    if((@swat_count % 10) == 0)
        show_message "Level Up!"
        @fly_spawn_rate *= 0.75
        if @fly_spawn_rate < 250
            end_game "YOU WIN!"
        end
    end
end

def show_message message_text
    message_sprite = $resources.create_text_sprite(message_text)
        .set_position(400, 650)
        .set_font_size(100)
    message_sprite.run_effect(Message_Show_And_Hide, At_End_Remove_Sprite)
end

def end_game message_text
    message_sprite = $resources.create_text_sprite(message_text)
                               .set_font_size(100)
                               .set_transparency(0)
                               .set_scale(0)
    message_sprite.run_effect(End_Game_Message)

    $system.sleep 5000
    $system.restart
end

show_message "Get Off My Pizza!"
$system.sleep 1000
spawn_fly
while($screen.update()) do end
