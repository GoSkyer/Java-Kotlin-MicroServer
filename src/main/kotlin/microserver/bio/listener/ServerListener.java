package microserver.bio.listener;


import microserver.bio.MicroConnection;
import microserver.bio.ServerConfig;
import microserver.bio.ServerState;

public abstract class ServerListener {

    public abstract void onCreate(ServerConfig config, ServerState state);

    public abstract void onStart(ServerConfig config, ServerState state);

    public abstract void onAccept(final MicroConnection connection);

    public abstract void onClose(ServerConfig config, ServerState state);

}

