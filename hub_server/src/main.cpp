#include "Log.hpp"
#include "Server.hpp"

#include <sstream>
#include <string>

#define DEFAULT_BUFLEN 512

char* key;

constexpr unsigned int str2int(const char* str, int h = 0)
{
    return !str[h] ? 5381 : (str2int(str, h+1) * 33) ^ str[h];
}

std::string concat(std::string string1, std::string string2) {
    std::stringstream ss;
    ss << string1 << string2;
    std::string result = ss.str();

    return result;
}

void check_connection(server::sockets _sockets) {

    log::log( "Sending code: 201", 0);
    server::_sendMsg(_sockets.ClientSocket, "201", 3);
    log::log("Code sent", 0);

    server::_shutdownSocket(_sockets.ClientSocket);
    log::log("Socket shutdown", 0);
}

void shutdown(server::sockets _sockets) {

    log::log( "Sending code: 210", 0);
    server::_sendMsg(_sockets.ClientSocket, "210", 3);
    log::log("Code sent", 0);

    server::_shutdownSocket(_sockets.ClientSocket);
    log::log("Socket shutdown", 0);

    system("shutdown -s -t 0");
}

void error(server::sockets _sockets) {

    log::log( "Sending error code: 001", 0);
    server::_sendMsg(_sockets.ClientSocket, "001", 3);
    log::log("Code sent", 0);
}

unsigned init(void) {

    server::sockets _sockets;

    char clientMsg[DEFAULT_BUFLEN]; 

    while(true) {
        log::log("Starting ListenSocket...", 0);
        _sockets = server::_initListenSocket("80");
        
        log::log("Listening...", 0);
        _sockets = server::_listen(_sockets);

        log::log("Accepting...", 0);
        _sockets = server::_accept(_sockets);

        
        log::log("Waiting client code", 0);
        server::_waitMsg(_sockets.ClientSocket, clientMsg, DEFAULT_BUFLEN);
        log::log(concat("Code received: ", clientMsg), 0);
        std::string substring = clientMsg;
        log::log(concat("SubString: ", substring.substr(0,3)), 0);
        switch (str2int(clientMsg))
        {
            case str2int("101"):   //check_connexion
                log::log("Checking app connection", 0);
                check_connection(_sockets);
                break;
            case str2int("110"):   //shutdown
                log::log("Executing shutdown", 0);
                shutdown(_sockets);
                break;
            default:
                log::log(concat("Unknown code: ", clientMsg), 1);
                error(_sockets);
        }
    }
    
}

int main(int argc, char* argv[]) {
    ShowWindow(GetConsoleWindow(), SW_MINIMIZE);
    log::log("----------------------------------", 0);
    log::log("HUB server started", 0);

    init();

    return 0; 
}
