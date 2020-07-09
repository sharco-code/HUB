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
    log::log("Servidor iniciado", 0);

    log::log("Iniciando ListenSocket...", 0);
    _sockets = server::_initListenSocket("80");
    
    log::log("Escuchando...", 0);
    _sockets = server::_listen(_sockets);

    log::log("Aceptando...", 0);
    _sockets = server::_accept(_sockets);

    
    log::log("Esperando mensaje del cliente", 0);
    server::_waitMsg(_sockets.ClientSocket, clientMsg, DEFAULT_BUFLEN);
    log::log(concat("Recibido: ", clientMsg), 0);
    
    if(clientMsg == "apagar") {
        log::log("apagar recibido",0);
    }

    char* msg = (char*)"check_complete";
    log::log( concat("Enviando mensaje: ", msg), 0);
    server::_sendMsg(_sockets.ClientSocket, msg, strlen(msg));
    log::log("Mensaje enviado", 0);

    server::_shutdownSocket(_sockets.ClientSocket);
    log::log("Servidor apagado\n", 0);

    return 0;
}

int main(int argc, char* argv[]) {

    init();

    return 0; 
}
