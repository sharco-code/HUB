#include "Log.hpp"
#include "Server.hpp"

#include <sstream>
#include <string>

#define DEFAULT_BUFLEN 512

char* key;

std::string concat(std::string string1, std::string string2) {
    std::stringstream ss;
    ss << string1 << string2;
    std::string result = ss.str();

    return result;
}

unsigned init(void) {

    server::sockets _sockets;

    char clientMsg[DEFAULT_BUFLEN]; 

    log::log("----------------------------------", 0);
    log::log("Servidor started", 0);

    log::log("Starting ListenSocket...", 0);
    _sockets = server::_initListenSocket("80");
    
    log::log("Listening...", 0);
    _sockets = server::_listen(_sockets);

    log::log("Accepting...", 0);
    _sockets = server::_accept(_sockets);

    
    log::log("Waiting client code", 0);
    server::_waitMsg(_sockets.ClientSocket, clientMsg, DEFAULT_BUFLEN);
    log::log(concat("Ccode received:: ", clientMsg), 0);
    
    char* response = "000";

     switch (clientMsg)
      {
         case "101":   //check_connexion
            log::log("Checking app connection", 0);
            check_connection();
            break;
         case "110":   //shutdown
            log::log("Executing shutdown", 0);
            shutdown();
            break;
         default:
            log::log("Unknown code: " + clientMsg, 1);
      }

    log::log( concat("Sending code: ", response), 0);
    server::_sendMsg(_sockets.ClientSocket, response, strlen(response));
    log::log("Code sent", 0);

    server::_shutdownSocket(_sockets.ClientSocket);
    log::log("Server shutdown\n", 0);

    return 0;
}

void check_connection() {
    char* response = "201";
}

void shutdown() {
    char* response = "210";
}

int main(int argc, char* argv[]) {

    init();

    return 0; 
}
