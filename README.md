# Arduino
This is basically the program that reads data from PIR sensor conected to arduino and draw them to graph, also at the same time saving it
to the oracle express database. I have commented methods inside database class, so you can start this app without oracle express installed on your PC. There are 2 different classes to start the app. First one is FalseData, it's for the situation if you don't
have an arduino nearby, just to check if it's working fine. Second one is Main, for program to work fine with this class you have to have
arduino conected to your PC. Dont forget to add jFreeChart and ocomm libraries co you can draw and read data from Arduino. That's all :)

You can see how does it look like down here. Created in Swing. 
![Example](Arduino_PIR.jpg "How does it look like")
