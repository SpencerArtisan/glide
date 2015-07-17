java_import com.bigcustard.blurp.model.System
java_import com.bigcustard.blurp.model.ImageSprite
java_import com.bigcustard.blurp.model.Image
java_import com.bigcustard.blurp.model.Camera
java_import com.bigcustard.blurp.model.Colour
java_import com.bigcustard.blurp.model.Keyboard
java_import com.bigcustard.blurp.model.Screen
java_import com.bigcustard.blurp.model.TextSprite
java_import com.bigcustard.blurp.model.Utils

class Wrapper
  def self.wraps
    [System, ImageSprite, Image, Camera, Colour, Keyboard, Screen, TextSprite, Utils]
  end

  def initialize subject
    @subject = subject
  end

  def method_missing m, *args
    if is_setter? m
      call_setter m, *args
    elsif is_getter? m, *args
      call_getter m
    else
      wrap_if_wrappable call_method m, *args
    end
  end

  def is_getter? name, *args
    args.length == 0 and has_field(name) and !name.to_s.end_with?('=')
  end

  def is_setter? name
    has_field(name) and name.to_s.end_with?('=')
  end

  def call_getter name
    field(name).get @subject
  end

  def call_setter name, *args
    field(name).set @subject, args[0]
  end

  def has_field name
    name = name.to_s.chomp '='
    @subject.getClass.getFields.any? {|field| field.getName == name}
  end

  def field name
    name = name.to_s.chomp '='
    @subject.getClass.getField name
  end

  def call_method name, *args
    @subject.send name, *args
  end

  def wrap_if_wrappable value
    wrappable?(value) ? Wrapper.new(value) : value
  end

  def wrappable? obj
    Wrapper.wraps.any? {|wrappable| obj.is_a? wrappable}
  end
end

{
  'blurp' => Wrapper.new($bindings.get "blurp"),
  'screen' => Wrapper.new($bindings.get "screen"),
  'keyboard' => Wrapper.new($bindings.get "keyboard"),
  'utils' => Wrapper.new($bindings.get "utils")
}