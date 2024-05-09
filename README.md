# video-game-user
Simple project to experiment with using package published to github packages securely

# Tools
## Sprite Editor
Anywhere in your code, as long as you have a ```requirements```, you can run the sprite editor.  For example, to modify sprite at index 2, run:

```
spriteEditor(requirements, 0)
```

The tool will allow you to edit the pixels of your sprite, providing code to encode the data for the pixels on the right hand side.

Note that you can also specify the name of the requirements variable, for example:

```
spriteEditor(requirements, 0, "myRequirements")
```

# Game Play
Games themselves are run through ```GameConsole```, in the Composables.kt file.