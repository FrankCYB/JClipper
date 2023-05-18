
# JClipper

A Java GUI for syncing clipboards across multiple computers.

![](https://i.ibb.co/VL8cFhX/JClipper.gif)
## Features

- Sync clipboards with multiple devices
- View your clipboard history (Works Offline)
- Compatible with legacy OS's
- Cross platform


## Setup


_To use JClipper, you can follow these steps:_

1. Download the [latest release](https://github.com/FrankCYB/JClipper/releases/latest "Latest release page") and extract the archieve.

2. Run the JClipper.jar from the folder you extracted.

3. When the program launches, you will be presented with the JClipper GUI window. You will see two radio buttons - one for Server and one for Client.

4. If you want to use JClipper as a Server, select the Server radio button and enter the port number you want the TCP server to run on. If you plan to use JClipper locally, take note of your local IP address, which will be used on the clients you want to connect to. If you plan to use JClipper over the internet, you will need to configure your router to allow incoming connections on the port you have specified.

5. If you want to use JClipper as a Client, select the Client radio button and enter the correct IP address and port number in the textbox field.

6. Once you have entered the necessary information, press the "Start" button on both the server and the client to initialize the JClipper program. 

7. Once JClipper is running, all clipboard content that is copied or cut on one computer will be automatically synchronized with the clipboard on the other computers.

8. If you want to view your previous clipboard copies, click on the Clipboard History button, which will show the last 25 clipboard entries.




## Optional Config

If you would like to apply additonal options to JClipper, you can include a config.properties in the directory of the Jar. Here is an example config:

```
ip=                     #Auto fill IP text field on launch with set IP
port=                   #Auto fill PORT text field on launch with set PORT
isServer=true           #Launch as server if true. Launch as client if false
isClipHistoryOn=true    #Enable or disable clipboard history
autostart=false         #Automatically start server/client on launch if true
```
## Requirements

- Java 5 or higher


## Final notes

If you enjoy JClipper and would like to support me in future updates and projects, please feel free to show your support by buying me a ☕ 

<a href="https://www.buymeacoffee.com/FrankCYB" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>

