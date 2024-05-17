# video-game-user
Simple project to experiment with using package published to github packages securely


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

The ```TileBaseedGameWorld``` contains a list of ```LevelModel```s.  A LevelModel is a grid of sprite tiles.  You can specify the sprite tiles that should go into a LevelModel as follows:

```
val levelModel = LevelModel(requirements, 10, 10)

//  Specify a single tile
levelModel.setTile(0, 0, 2)

//  Specify the tiles of an entire row
levelModel.setSpritesOnRow(0, listOf(
    2, 2, 2, 2, 2, 10, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2
))
```

## Defining your Game's Logic
Game logic is typically written in a game model.  The game model is then interacted with by an object implementing the ```VideoGameController``` interface.

There are some example games that can be found in the ```com.vandenbreemen.videogameusr.game``` package.

# Tools

This project provides some tools to make creating game graphics more visually interactive than hand-coding sprites.  You can access these by calling the ```gameEditor``` function and passing in the appropriate objects (requirements, etc).

A good example of this can be found in VideoGame4.kt.

## Sprite Editor
Anywhere in your code, as long as you have a ```requirements```, you can run the sprite editor.  For example, to modify sprite at index 2, run:

```
gameEditor(requirements, 0)
```

The tool will allow you to edit the pixels of your sprite, providing code to encode the data for the pixels on the right hand side.

Note that you can also specify the name of the requirements variable, for example:

```
spriteEditor(requirements, 0, "myRequirements")
```

## Level Editor
The tool also comes with a level editor, allowing you to define multiple levels with sprite tiles on them.  This lets you create game environments visually.

### A Note about Code Generation
While the tool's code supports code generation I am slowly phasing this out since most game levels will create code that is too large to be compiled in a single method in Java/Kotlin.  Instead, the tool provides a game asset data file format.


> **Tip**:  To generate an asset sheet for the sprites/levels you have made simply open up the hamburger menu and select "Dump Assets to File." 
