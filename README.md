# Inversion 

Inversion aims to be a video processing app for Android, currently in development. Its aims to perform real-time video processing on the GPU, 
allowing users to add special effects to their videos quickly and save them for sharing. 

## Visual Prototypes

![Prototype](https://i.imgur.com/EDM3uef.png "Screenshots of the app prototype")

Link to the GIF prototype demo: https://i.imgur.com/wPW9auh.gif 

## Development Notes
The app is being developed with Android Studio, written in Java, and supports mobile devices with a minimum API Level of 21. 

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
- **Filters**: Hue transforms, monochrome, sepia, blending with a variety of gradients, RGB shifts, tints, 
- **Special effects**: Other effects such as applying vignettes, halftone, pixelization, noise, vintage effects, solarization etc.,
- **Blending**: Blending with other images will also be available, such as using Multiply blend, Add blend, and so forth. 

## Other Features
- Image collage creation
- Adding text to images
- Watermarking videos
- Exporting edited images/videos

It is hoped that at least 50 filters/effects will be available for use in both the image editor and video editor.
Some visual examples of the filters will be added here or in the repository Wiki. 