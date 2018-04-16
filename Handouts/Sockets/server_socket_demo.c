// Setting up a server
// DSM, 2017
// Incorporates code from Beej's Guide to Network Programming

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

#define PORT "8080" // The server listens for new connections on this port
#define BACKLOG 10  // Max number of pending connection to queue

int main(void) {
  
    // Initialize the address structure
    struct addrinfo *servinfo;
    struct addrinfo hints;

    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;  // Use either IPv4 or IPv6
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP

    int rc = getaddrinfo(NULL, PORT, &hints, &servinfo);
    if (rc < 0) {
        perror("getaddrinfo");
        return 1;
    }

    // Create the socket
    int server_fd = socket(servinfo->ai_family, servinfo->ai_socktype, 
                           servinfo->ai_protocol);
    if (server_fd < 0) {
        perror("server: socket");
    }

    // Bind the socket
    rc = bind(server_fd, servinfo->ai_addr, servinfo->ai_addrlen);
    if (rc < 0) {
        close(server_fd);
        perror("server: bind");
    }
  
    // Listen for incoming connections
    rc = listen(server_fd, BACKLOG);
    if (rc < 0) {
        perror("listen");
        return(-1);
    }

    // Server is now up and waiting for requests

    // Accept incoming requests
    printf("Server is running and will accept incoming requests.\n\n");

    // A buffer to receive messages
    char buf[1200];

    // Loop, accepting and processing incoming messages
    while (1) {
  
        // accept blocks until a client connection arrives
        socklen_t sa_len = sizeof(servinfo);
        int connection_fd = accept(server_fd, (struct sockaddr*) &servinfo, 
                                   &sa_len);
        if (connection_fd < 0) {
            perror("accept");
            return(-1);
        }
  
        // connection_fd is a new descriptor that refers to the connection the
        // server has just received
        // Two descriptors in play: the server's -- server_fd
        //                          the connection's -- connection_fd

        printf("Accepted a request.  Descriptor = %d\n", connection_fd);

        rc = read(connection_fd, buf, sizeof(buf));
        printf("Message:\n\n%s\n", buf);

        // Add some code to write a response back to the client

        // Close the connection
        rc = close(connection_fd);
        if (rc < 0){
            perror("close");
            return(-1);
        }
    }

    return 0;
}