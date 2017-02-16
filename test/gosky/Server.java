package gosky;


import org.goskyer.microserver.bio.MicroServer;

/**
 * Created by galaxy on 2017/2/14.
 */
public class Server {

    public static void main(String[] args) {
        MicroServer server = new MicroServer();
        server.initServer();
        server.startServer();
        MicroServer serverKt = new MicroServer();
    }

}
