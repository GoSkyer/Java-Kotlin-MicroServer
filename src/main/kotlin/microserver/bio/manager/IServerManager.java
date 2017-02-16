package microserver.bio.manager;


import microserver.bio.MicroServer;
import microserver.bio.listener.ConnectionListener;

/**
 * Created by galaxy on 2017/2/9.
 */

public interface IServerManager {

    boolean open(int port);

    boolean send(int port, String args);

    void addPortListen(int port, ConnectionListener listener);

    void removePortListen(int port);

    void close(int port);

    void closeAll();

    MicroServer getServer(int port);

}
