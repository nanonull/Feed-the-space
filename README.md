# Feed-the-space
Game created for [Libgdx Jam 2015](http://itch.io/jam/libgdxjam).<br>
This is top-down 2d arcade about spaceship which arrives to unknown solar system after warp-jump.<br>
It uses: Libgdx java framework (game engine), box2d library (physics) , artemis-odb library (ESC engine).<br>

![screen](http://cs630221.vk.me/v630221319/eb85/3USpEmjLmDI.jpg "Logo Title Text 1")

## Run game
Requires [Java 8 JRE](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html).<br>
Language (en/ru) and Fullscreen/Windowed mode could be changed in common.properties file.<br>

#### Download game:
[Game homepage] (http://them.itch.io/feed-the-space)

#### From sources:
Download/clone sources.<br>
Run ClientApplication class in 'spashole-game' module.<br>

##### PS:
[Devlog](http://itch.io/jam/libgdxjam/topic/12183/feed-the-space-by-them-devlog#post-6444)<br>
It took ~150 hours to create it (however, some basic components, like custom 2d window, had been created before jam).<br>
~6k lines of code<br>

##### What intresting:
- how to organize entities and components in ECS;
- how to synchronize physics world (box2d) with view application (libgdx render);
- how to work with libgdx stage/actors/ui;
- how to organize text/quest dialogs;
- how to organize localized resources;
- how to use gradle multi-module project;
- how to convert overlap2d scene into game entities;
- how to create simple repl with custom commands;
