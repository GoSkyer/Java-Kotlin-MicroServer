package microserver.bio.listener;


import microserver.bio.MicroConnection
import microserver.bio.ServerConfig
import microserver.bio.ServerState

abstract class ServerListener {

    abstract fun onCreate(config: ServerConfig, state: ServerState);

    abstract fun onStart(config: ServerConfig, state: ServerState);

    abstract fun onAccept(connect: MicroConnection)

    abstract fun onClose(config: ServerConfig, state: ServerState)

}

