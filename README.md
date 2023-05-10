## Information

This is a project to showcase the use of Sockets in Java, using Threads and Synchronization.

## Details

In this Restaurant, we have Chefs that specialize in making specific dishes (Fish, Seafood and Meat).

- A customer selects the Chef (type of food) they want and orders the number of portions they desire

- The Chef takes the time required to prepare the dishes and serves them when ready

- Each Chef is limited to preparing 20 portions per shift and can only have a maximum of 5 dishes ready at a time

- The orders from the customers are repeated until no more dishes can be prepared

At this point the program releases resources and stops executing.

Connection errors and wrong responses are handled by printing the according messages and releasing resources.

## Instructions
To run the program quickly, download only the two .jar files from the /dist folder of the Server and Client projects.
Execute them through cmd (two separate windows) with the `java -jar <application_name>.jar` command, where application_name is replaced with the name of the file accordingly.

If you are afraid of executing my .jar files, you can create your own from scratch by making a clean build for each of them through a Java IDE (e.g. Apache NetBeans).
That means you first have to download both projects' source code and load them to the IDE of your choice.

## Topics
- Distributed Systems
- Java
- Sockets
- Threads
- Synchronized methods
- Jar files

## Technologies Used
- Java 17
- Apache NetBeans

## Notes
University project for the course of Distributed Systems

Have fun! 😎