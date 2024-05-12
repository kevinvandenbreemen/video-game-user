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

### A Note about Code Generation for Lots of Sprites
While you use the sprite editor the tool willl create an entire "sprite sheet" detailing how to draw all of the sprites your ```GameDataRequirements``` object allows for in a file in a file called ```generated/SpriteData.kt```.  If you are making lots of edits you can simply open that file and copy/paste the contents into your game.

> **Tip**:  You can tweak this behaviour by modifying the SpriteCodeGenerationInteractor class. 

# Game Play
Games themselves are run through ```GameConsole```, in the Composables.kt file.

# Game Development
## Defining your Game's Storage Space
Raw data about game assets are stored in ```GameDataRequirements``` object.  This object defines the size of the screen a frame of the game is to be
displayed on along with the dimensions of a single sprite (or tile) of game data.  Lastly it defines the number of bytes that can be used to store the game assets.

## Defining your Game's Assets
You add a sprite to the ```GameDataRequirements``` using the setData() method.  This method takes in the index of the sprite, the data for the sprite

The following example comes from game 1 and defines the "alien" the player needs to get away from or shoot:

```kotlin
    requirements.setData(2, byteArrayOf(
        -125, 0, 0, 0, 0, 0, 0, -125,
        0, 120, 0, 0, 0, 0, 120, 0,
        0, 0, 100, -16, -16, 100, 0, 0,
        0, 100, 64, -52, -52, 64, 100, 0,
        100, 100, 12, 12, 12, 100, 100, 100,
        12, 0, 12, 12, 12, 0, 12, 12,
        0, 0, 12, 0, 0, 12, 0, 0,
        0, 12, 0, 0, 0, 0, 12, 0
    ))
```

This code creates a sprite at sprite index 2 and stores it to the game requirements.

As discussed earlier there is a sprite editor you can use to edit a tile.

## Tile Based Game
If you are creating a tile based game you can use the convenient ```TileBasedGameWorld``` type to define where your tiles should go in the game world.  This type provides a series of levels, which consist of grids of tiles.

## Defining your Game's Logic
Game logic is typically written in a game model.  The game model is then interacted with by an object implementing the ```VideoGameController``` interface.

There are some example games that can be found in the ```com.vandenbreemen.videogameusr.game``` package.