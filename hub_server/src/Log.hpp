#ifndef LOG_HPP
#define LOG_HPP

#include <fstream>
#include <ctime>
#include <iostream>

using namespace std;

namespace log {

    inline string getCurrentDateTime( string s ){
        time_t now = time(0);
        struct tm  tstruct;
        char  buf[80];
        tstruct = *localtime(&now);
        if(s=="now")
            strftime(buf, sizeof(buf), "%Y-%m-%d %X", &tstruct);
        else if(s=="date")
            strftime(buf, sizeof(buf), "%Y-%m-%d", &tstruct);
        return string(buf);
    };

    inline void Logger( string logMsg ){

        string filePath = "log_"+getCurrentDateTime("date")+".txt";
        string now = getCurrentDateTime("now");
        ofstream ofs(filePath.c_str(), std::ios_base::out | std::ios_base::app );
        ofs << now << '\t' << logMsg << '\n';
        ofs.close();
    }

    void log(std::string msg, bool err) {
        std::string _msg;

        if(!err)  _msg = "[LOG] " + msg;
        else _msg = "[ERROR] " + msg;

        std::cout << _msg << std::endl;

        Logger(_msg);
    }
    
}
#endif