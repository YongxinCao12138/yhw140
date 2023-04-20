# assignment2

## compile
```
cd src

javac Client.java

javac Server.java
```
you can see Client.class Server.class in this directory, you success

## run
```
java Server
```
to run a server, use default port 8000, 
you can see a log print on terminal

```
java Client localhost 8000
```

Client doesn't have default host and port, enter parameters at startup host and port,
you can see a log print on client terminal and server terminal

Follow the prompts to enter the number of simulation nodes

You can see that during the simulation process, on the server terminal.
The client can obtain simulation results

Client input q or quit to end, Of course, you can also kill this process and force it to exit.
Server can perceive and print logs, and serve multiple clients but sequentially, one after the other.