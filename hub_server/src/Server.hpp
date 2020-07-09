#ifndef SERVER_HPP
#define SERVER_HPP

#undef UNICODE

#define _WIN32_WINNT 0x501
#define WIN32_LEAN_AND_MEAN

#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <stdio.h>

#pragma comment (lib, "Ws2_32.lib")

namespace server {

    struct sockets {
        SOCKET ListenSocket;
        SOCKET ClientSocket;
    };

    //devuelve ListenSocket y ClientSocket a traves del struct
    sockets _initListenSocket(const char* PORT) {

        sockets sockets;

        WSADATA wsaData;

        struct addrinfo *result = NULL;
        struct addrinfo hints;

        int i;
        i = WSAStartup(MAKEWORD(2,2), &wsaData);
        if (i != 0) {
            printf("WSAStartup failed with error: %d\n", i);
        }

        ZeroMemory(&hints, sizeof(hints));
        hints.ai_family = AF_INET;
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_protocol = IPPROTO_TCP;
        hints.ai_flags = AI_PASSIVE;

        i = getaddrinfo(NULL, PORT, &hints, &result);
        if ( i != 0 ) {
            printf("getaddrinfo failed with error: %d\n", i);
            WSACleanup();
        }

        sockets.ListenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);
        if (sockets.ListenSocket == INVALID_SOCKET) {
            printf("socket failed with error: %ld\n", WSAGetLastError());
            freeaddrinfo(result);
            WSACleanup();
        }

        i = bind(sockets.ListenSocket, result->ai_addr, (int)result->ai_addrlen);
        if (i == SOCKET_ERROR) {
            printf("bind failed with error: %d\n", WSAGetLastError());
            freeaddrinfo(result);
            closesocket(sockets.ListenSocket);
            WSACleanup();
        }

        freeaddrinfo(result);

        return sockets;
    }

    sockets _listen(sockets sockets) {
        int i;
        i = listen(sockets.ListenSocket, SOMAXCONN);
        if (i == SOCKET_ERROR) {
            printf("listen failed with error: %d\n", WSAGetLastError());
            closesocket(sockets.ListenSocket);
            WSACleanup();
        }

        return sockets;
    }

    sockets _accept(sockets sockets) {

        sockets.ClientSocket = accept(sockets.ListenSocket, NULL, NULL);
        if (sockets.ClientSocket == INVALID_SOCKET) {
            printf("accept failed with error: %d\n", WSAGetLastError());
            closesocket(sockets.ListenSocket);
            WSACleanup();
        }

        closesocket(sockets.ListenSocket);
        
        return sockets;
    }
    
    void _waitMsg(SOCKET ClientSocket, char* msg, int msglen) {
        recv(ClientSocket, msg, msglen, 0);
    }

    char* _sendMsg(SOCKET ClientSocket, char* msg, int msglen) {
        send(ClientSocket, msg, msglen, 0); 
        return msg;
    }

    int _shutdownSocket(SOCKET ClientSocket) {
        int i;
        i = shutdown(ClientSocket, SD_SEND);
        if (i == SOCKET_ERROR) {
            printf("shutdown failed with error: %d\n", WSAGetLastError());
            closesocket(ClientSocket);
            WSACleanup();
            return 1;
        }
        closesocket(ClientSocket);
        WSACleanup();
        return 0;
    }

}

#endif
