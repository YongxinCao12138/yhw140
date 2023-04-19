package util;

import java.net.Socket;

public class SocketUtil {
    // forbid Util constructor
    private SocketUtil() {}

    /**
     * is connect
     * disconnect = true
     * connect = false
     */
    public static Boolean isConnectClose(Socket socket){
        try{
            // send a urgentData
            socket.sendUrgentData(0xFF);
            return false;
        }catch(Exception se){
            return true;
        }
    }
}
