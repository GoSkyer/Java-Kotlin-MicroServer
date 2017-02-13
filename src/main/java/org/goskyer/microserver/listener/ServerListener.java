package org.goskyer.microserver.listener;


import org.goskyer.microserver.MicroConnection;
import org.goskyer.microserver.ServerConfig;
import org.goskyer.microserver.ServerState;

public abstract class ServerListener {

    public abstract void onCreate(ServerConfig config, ServerState state);

    public abstract void onStart(ServerConfig config, ServerState state);

    public abstract void onAccept(final MicroConnection connection);

    public abstract void onClose(ServerConfig config, ServerState state);

}

