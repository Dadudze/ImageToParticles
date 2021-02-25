# ImageToParticles
Turns image into particles

Can be used for displaying in-game emotes

Quick start:
```
ITP.drawRotatedImage(player.getEyeLocation(), player.getLocation().getYaw(), 1, 2, new File("image.png"), Color.WHITE);
```
Will draw image rotated by player's yaw, if you already know, where to draw image you should use:
```
ITP.drawImage(new File("image.png"), player.getEyeLocation().subtract(1, 0, 0), player.getEyeLocation().add(1, 2, 0), Color.WHITE);
```

If your image is too big you can use `ITP.resize(image, width, height)`
