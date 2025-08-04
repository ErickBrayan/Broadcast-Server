# Broadcast Server CLI Application
This is a simple broadcast server that will allow clients to connect to it, send messages that will be broadcasted to all connected clients

## Features

- Create a server that listens for incoming connections.
- When a client connects, store the connection in a list of connected clients.
- When a client sends a message, broadcast this message to all connected clients.
- Handle client disconnections and remove the client from the list of connected clients.
- Implement a client that can connect to the server and send messages.

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/ErickBrayan/Broadcast-Server.git 

2. **Change Directory:**
   ```bash
   cd sockets/src

3. **Compile the source code:**
    ```bash
   javac BroadcastApp.java
4. **Run the application:**
    ```bash
   java BroadcastApp start/connect
   ```

## Usage
```bash
# Start Server
java java BroadcastApp start

# Connect to the server as Client
java BroadcastApp connect
```


![Capture](/Capture.png)

https://roadmap.sh/projects/broadcast-server
