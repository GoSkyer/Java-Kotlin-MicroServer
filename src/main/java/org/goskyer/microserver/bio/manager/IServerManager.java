package org.goskyer.microserver.bio.manager;


import org.goskyer.microserver.bio.MicroServer;
import org.goskyer.microserver.bio.listener.ConnectionListener;

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
