Smartwatch Rocket Launcher
==========================

Application to launch a model rocket from a smart watch using an Arduino-based igniter

[![Demo Video Image](https://raw.githubusercontent.com/jose-troche/Documentation/master/SmartWatchRocketLauncher/smartwatch-rocket-launcher-video.png)]
(//www.youtube.com/embed/jpNanZiIaBE?rel=0)

Hardware
========

* Smartwatch with Android Wear support
* Arduino UNO
* Bluetooth shield (http://www.seeedstudio.com/wiki/Bluetooth_Shield)
* Relay shield (http://www.seeedstudio.com/wiki/Relay_Shield_V2.0)
* Smartphone (optional)

Architecture
============

You need an Arduino UNO coupled with a Bluetooth module (shield) and a relay module. The Arduino program waits for a command wirelessly transmitted via Bluetooth. The command will turn on the relay switch allowing the current of regular AA batteries to flow and ignite the rocket engine.

The originator of the command can be any Bluetooth-enabled device, like a computer, a cell phone or... a smartwatch! The code includes an Android Wear application. Since the smartwatch supports voice commands, you can basically say: "OK google... START ROCKET IGNITION" and the rocket takes off!

![architecture](https://raw.githubusercontent.com/jose-troche/rocket-launcher/master/docs/img/RocketLauncher.png)
