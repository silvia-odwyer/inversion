# Inversion 

Inversion is a video and image processing app for Android, which performs real-time video processing on the GPU,
allowing users to add special effects to their videos/images quickly and save them for sharing.

## Screenshots

### Video Editor Screenshots
![Video Editor screenshots](https://github.com/silvia-odwyer/inversion/blob/master/screenshots/video_editor_screenshots.PNG "Video Editor Screenshots")

![Video Editor screenshots](https://github.com/silvia-odwyer/inversion/blob/master/screenshots/video_editor_screenshots2.PNG "Video Editor Screenshots")


### Image Editor Screenshots
![Image Editor screenshots](https://github.com/silvia-odwyer/inversion/blob/master/screenshots/image_editor_screenshots.PNG "Image Editor Screenshots")

![Image Editor screenshots](https://github.com/silvia-odwyer/inversion/blob/master/screenshots/image_editor_screenshots2.PNG "Image Editor Screenshots")

## Development Notes
The app was developed with Android Studio, written in Java, and supports mobile devices with a minimum API Level of 21. 

### Screens/Activities
- **Home**: This is the app’s Launcher activity, and is the starting point for the user. It contains a selection of the user’s images and videos which they have previously edited. It also contains a list of new effects which have been added recently. 

- **Images**: Displays all images edited by the user. The user can sort the images in chronological or alphabetical order. If a user clicks on an image, they will be directed to the Image Editor activity. 

- **Image Editor**: This is the activity for the app's image editor, and the user can apply a variety of effects and filters to the image they have selected, including those mentioned below.

- **Videos**: Displays all videos edited by the user, sorted by either chronological or alphabetical order. If a user selects a video, they will then be brought 
to the Video Editor screen. 

- **Video Editor**: This activity allows the user to apply a variety of special effects and filter to their videos, including tinted filters, blending 
with gradients, correction, as well as all those effects mentioned below.

- **Effects**: Lists all filters and special effects available to the user. 

- **Effect Detail**: If a user selects an effect name from the `Effects` activity, they will be brought to this activity which contains details 
about the effect and allows the user to preview the effect on an image.

- **Settings**: Allows the user to change properties of the app, such as its theme, etc.,

This list is not complete as of yet, for example if new screens or activities are required they will be added here.

## Filters 
- **Image correction**: Sharpen, blur, adjustments to saturation, contrast, brightness, etc.,
- **Vintage**: Monochrome, sepia, and vignettes add a vintage effect to images
- **Retro**: Pop art, lomo, and dust overlays add a retro atmosphere to images
- **Gradients**: Blends including gradients are also available
- **Glitch**: These filters add a futuristic flair to images, and include RGB shifts, chromatic
aberration, monitor scanlines, and other glitch effects.
- **Filters**: Hue transforms, blending with a variety of gradients, RGB shifts, tints,
- **Special effects**: Other effects such as applying vignettes, halftone, pixelization, noise, vintage effects, solarization etc.,
- **Blending**: Blending with other images will also be available, such as using Multiply blend, Add blend, and so forth.