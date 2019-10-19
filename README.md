# QT-HotS
**QT**-HotS (pronounced *cute HotS*) is an ocr-based (**o**ptical **c**haracter **r**ecognition) **Q**uest **T**racker application
written in Java.
Just run it while you are playing Heroes of the Storm to track your ingame talent quest progress.

This was made by two developers. 

## Things I learned developing this
* Taking Screenshots with Java
* Using OCR (Tesseract)
* Working with JavaFX

## Requirements
Java 8-10

If you want to use Java 11 or higher you have to install JavaFX manually

## Known Problems
At this point, everything works fine :)

## Caveats
OCR ([optical character recognition](https://en.wikipedia.org/wiki/Optical_character_recognition)) is a bit resource
intensive. If you experience lags, your PC might be too slow for this tool to run smoothly.

## How To Contribute
You can always contribute to this project via pull-request to keep it up-to-date

### Things that need to be updated regularly:
- Hero (for suggestions)
- Talents (for suggestions)

### Screenshot Areas For OCR
Once every 5 seconds the tool takes screenshots of the game and runs those through the ocr-software.
In order to achieve the best and most reliable results, we have hardcoded values for several screensizes.
All the other sizes will be calculated dynamically (soon tm) which might not be correct in every scenario.

**Supported:** 

- 1440p (2560 x 1440)
- 1080p (1920 x 1080)

If you play at a different resolution, you can contribute by adding the correct screenshot area values.
Please add test images in your resolution to the `exampleimages/quests/YOUR_RESOLUTION` and
`exampleimages/timers/YOUR_RESOLUTION` folders and make sure the tests pass.

## Future Plans 
In the future we'd like to add a webservice to automatically upload your data and the replay to. This webservice
will analyze the replay and fill in hero and talent names
