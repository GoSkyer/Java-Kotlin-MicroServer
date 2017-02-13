package org.goskyer.microserver.bio.listener;


import org.goskyer.microserver.bio.MicroConnection;
import org.goskyer.microserver.bio.ServerConfig;
import org.goskyer.microserver.bio.ServerState;

public abstract class ServerListener {

    public abstract void onCreate(ServerConfig config, ServerState state);

    public abstract void onStart(ServerConfig config, ServerState state);

    public abstract void onAccept(final MicroConnection connection);

    public abstract void onClose(ServerConfig config, ServerState state);

}

